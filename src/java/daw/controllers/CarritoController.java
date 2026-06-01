package daw.controllers;

import daw.model.Actividad;
import daw.model.Usuario;
import daw.util.ViewUtils;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@WebServlet(name = "CarritoController", urlPatterns = {"/carrito", "/agregarCarrito", "/eliminarCarrito", "/inscribirseCarrito"})
public class CarritoController extends HttpServlet {

    @PersistenceContext(unitName = "AgendaPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        String action = request.getServletPath();

        // Mostrar el carrito
        if ("/carrito".equals(action)) {
            // Obtener el carrito de la sesion
            List<Actividad> carrito = (List<Actividad>) request.getSession().getAttribute("carrito");

            // Si no hay carrito, inicializar uno vacio
            if (carrito == null) {
                carrito = new ArrayList<>();
            }
            ViewUtils.vistaTemplate(request, response, usuario, "Carrito");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        // Agregar actividad al carrito
        if ("/agregarCarrito".equals(action)) {
            Long idActividad = Long.valueOf(request.getParameter("idActividad"));

            // Recuperamos la actividad desde la base de datos
            TypedQuery<Actividad> query = em.createNamedQuery("Actividad.findById", Actividad.class)
                    .setParameter("id", idActividad);
            Actividad actividad = query.getSingleResult();

            if (actividad != null) {
                // Obtener el carrito de la sesion, o crear uno vacio si no existe
                List<Actividad> carrito = (List<Actividad>) request.getSession().getAttribute("carrito");
                if (carrito == null) {
                    carrito = new ArrayList<>();
                }

                // Verificar si la actividad ya esta en el carrito
                boolean yaExiste = false;
                for (Actividad a : carrito) {
                    if (a.getId().equals(actividad.getId())) {
                        yaExiste = true;
                        break;
                    }
                }

                // Si la actividad no esta en el carrito, agregarla
                if (!yaExiste) {
                    carrito.add(actividad);
                    request.getSession().setAttribute("carrito", carrito);
                }
            }

            // Redirigir al carrito
            response.sendRedirect(request.getContextPath() + "/carrito");
        }

        // Eliminar actividad del carrito
        if ("/eliminarCarrito".equals(action)) {
            Long idActividad = Long.valueOf(request.getParameter("idActividad"));
            List<Actividad> carrito = (List<Actividad>) request.getSession().getAttribute("carrito");

            if (carrito != null) {

                Iterator<Actividad> iterator = carrito.iterator();
                while (iterator.hasNext()) {
                    Actividad a = iterator.next();
                    if (a.getId().equals(idActividad)) {
                        iterator.remove(); // Eliminar el elemento actual
                    }
                }
                request.getSession().setAttribute("carrito", carrito);
            }
            response.sendRedirect(request.getContextPath() + "/carrito");
        }

        // Inscribir al usuario en las actividades del carrito
        if ("/inscribirseCarrito".equals(action)) {
            List<Actividad> carrito = (List<Actividad>) request.getSession().getAttribute("carrito");

            if (carrito != null) {
                Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

                if (usuario != null) {
                    try {
                        // Inscribir al usuario en todas las actividades del carrito
                        for (Actividad actividad : carrito) {
                            inscribirUsuarioEnActividad(usuario.getId(), actividad.getId());
                        }
                    } catch (Exception ex) {
                        request.setAttribute("msg", "Error: " + ex.getMessage());
                        ViewUtils.vistaTemplate(request, response, usuario, "error");
                    }
                    // Limpiar el carrito después de la inscripcion
                    request.getSession().removeAttribute("carrito");

                    // Redirigir a una pagina de actividades inscritas
                    response.sendRedirect(request.getContextPath() + "/actividadesInscritas");
                }
            }
        }
    }

    public void inscribirUsuarioEnActividad(Long usuarioId, Long actividadId) throws Exception {
        try {
            // Iniciar la transaccion
            utx.begin();

            // Recuperar el usuario y la actividad
            Usuario usuario = em.createNamedQuery("Usuario.findById", Usuario.class)
                    .setParameter("id", usuarioId)
                    .getSingleResult();
            Actividad actividad = em.createNamedQuery("Actividad.findById", Actividad.class)
                    .setParameter("id", actividadId)
                    .getSingleResult();

            // Verificar que no este ya inscrito
            if (!usuario.getActividades().contains(actividad)) {
                // Agregar la actividad al usuario
                usuario.getActividades().add(actividad);
                // Agregar el usuario a la actividad
                actividad.getUsuarios().add(usuario);

                // Persistir los cambios
                em.merge(usuario);
                em.merge(actividad);
            }

            // Confirmar la transaccion
            utx.commit();
        } catch (Exception e) {
            if (utx != null) {
                utx.rollback();
            }
            throw e;
        }
    }

}
