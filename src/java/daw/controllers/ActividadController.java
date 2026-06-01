package daw.controllers;

import daw.model.Actividad;
import daw.model.Usuario;
import daw.util.ViewUtils;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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
import java.util.List;

@WebServlet(name = "ActividadController", urlPatterns = {"/crearActividad", "/modificarActividad", "/catalogoActividades", "/actividadesInscritas", "/vistaActividad", "/eliminarActividad", "/desapuntarseActividad"})
public class ActividadController extends HttpServlet {

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
        String vista;
        try {
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) { // Peticion de Fetch
                // Recuperar la lista de actividades
                List<Actividad> actividades = em.createNamedQuery("Actividad.findAll", Actividad.class).getResultList();

                // Generar la respuesta HTML
                StringBuilder htmlResponse = new StringBuilder();
                htmlResponse.append("<tbody>");

                if (actividades.isEmpty()) {
                    htmlResponse.append("<tr><td colspan='4'>No hay actividades disponibles.</td></tr>");
                } else {
                    for (Actividad actividad : actividades) {
                        htmlResponse.append("<tr>");
                        htmlResponse.append("<td>").append(actividad.getNombre()).append("</td>");
                        htmlResponse.append("<td>").append(actividad.getMonitor().getName()).append("</td>");
                        htmlResponse.append("<td>").append(actividad.getPrecio()).append("</td>");
                        htmlResponse.append("<td>");
                        htmlResponse.append("<form action='").append(request.getContextPath()).append("/vistaActividad' method='get'>");
                        htmlResponse.append("<input type='hidden' name='idActividad' value='").append(actividad.getId()).append("'>");
                        htmlResponse.append("<input type='submit' value='Ver Detalles'>");
                        htmlResponse.append("</form>");
                        htmlResponse.append("</td>");
                        htmlResponse.append("</tr>");
                    }
                }

                htmlResponse.append("</tbody>");
                response.setContentType("text/html;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");

                response.getWriter().write(htmlResponse.toString());
                return;
            } else if ("/crearActividad".equals(action)) {

                TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.tipo = :tipo", Usuario.class);
                query.setParameter("tipo", "Admin");
                List<Usuario> monitores = query.getResultList();
                request.setAttribute("usuarios", monitores);

                ViewUtils.vistaTemplate(request, response, usuario, "Actividades/formCrearActividad");

            } else if ("/catalogoActividades".equals(action)) {
                ViewUtils.vistaTemplate(request, response, usuario, "Actividades/catalogoActividades");

             
            } else if ("/actividadesInscritas".equals(action)) {
                // Cargar actividades inscritas
                TypedQuery<Actividad> query = em.createNamedQuery("Usuario.findActividadesByUsuarioId", Actividad.class);
                query.setParameter("usuarioId", usuario.getId());
                List<Actividad> actividades = query.getResultList();

                request.setAttribute("actividades", actividades);
                ViewUtils.vistaTemplate(request, response, usuario, "Actividades/actividadesInscritas");

            } else if ("/vistaActividad".equals(action)) {
                // cargar detalles de la actividad seleccionada
                String idActividadStr = request.getParameter("idActividad");

                if (idActividadStr == null || idActividadStr.isEmpty()) {
                    // Si el parametro no esta presente redirigimos al catalogo de actividades
                    response.sendRedirect(request.getContextPath() + "/catalogoActividades");
                    return;
                }
                try {
                    Long idActividad = Long.valueOf(idActividadStr);

                    Actividad actividad = em.createNamedQuery("Actividad.findById", Actividad.class)
                            .setParameter("id", idActividad)
                            .getSingleResult();

                    if (actividad == null) {
                        // Si no se encuentra la actividad, redirigir al catalogo
                        response.sendRedirect(request.getContextPath() + "/catalogoActividades");
                        return;
                    }
                    request.setAttribute("actividad", actividad); //enviamos la actividad q puede usar los distintos tipos de vista
                    if (usuario == null) {
                        // Vista para deslogueados;
                        ViewUtils.vistaTemplate(request, response, usuario, "Actividades/vistaActividadLogOut");
                        return;
                    }
                    Actividad a = null;
                    boolean inscritoEnActividad = true;
                    try {

                        a = em.createNamedQuery("Actividad.findByUsuarioIdAndActividadId", Actividad.class)
                                .setParameter("usuarioId", usuario.getId())
                                .setParameter("actividadId", idActividad)
                                .getSingleResult();

                    } catch (NoResultException e) {
                        inscritoEnActividad = false;
                    }
                    request.setAttribute("inscrito", inscritoEnActividad);
                    String jspSelecto;
                    if ("Admin".equals(usuario.getTipo())) {

                        request.setAttribute("usuarios", actividad.getUsuarios()); // añadimos la lista de usuarios
                        jspSelecto = "Actividades/vistaActividadPersonal"; // Vista para admin
                    } else {
                        jspSelecto = "Actividades/vistaActividadUsuarios"; // Vista para usuarios
                    }
                    ViewUtils.vistaTemplate(request, response, usuario, jspSelecto);
                } catch (Exception e) {
                    request.setAttribute("msg", e.getMessage());
                    ViewUtils.vistaTemplate(request, response, usuario, "error");
                }

            } else if ("/modificarActividad".equals(action)) {
                if ("Admin".equals(usuario.getTipo())) {
                    try {
                        String idParam = request.getParameter("id");
                        Long id = Long.parseLong(idParam);

                        TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByTipo", Usuario.class);
                        query.setParameter("tipo", "Admin");
                        List<Usuario> monitores = query.getResultList();

                        TypedQuery<Actividad> query2 = em.createNamedQuery("Actividad.findById", Actividad.class);
                        query2.setParameter("id", id);
                        Actividad actmod = query2.getSingleResult();

                        request.setAttribute("actividad", actmod);
                        request.setAttribute("monitores", monitores);
                        request.setAttribute("monitor", actmod.getMonitor());

                        ViewUtils.vistaTemplate(request, response, usuario, "Actividades/formModificarActividad");

                    } catch (Exception e) {

                    }
                }
            }
        } catch (Exception e) {
            request.setAttribute("msg", e.getMessage());
            ViewUtils.vistaTemplate(request, response, usuario, "error");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if ("/crearActividad".equals(action)) {

            try {
                String nombre = request.getParameter("nombre");
                String descripcion = request.getParameter("descripcion");
                Long monitorId = Long.valueOf(request.getParameter("monitor"));
                double precio = Double.parseDouble(request.getParameter("precio"));

                // Buscar el monitor por ID
                TypedQuery<Usuario> monitorQuery = em.createNamedQuery("Usuario.findById", Usuario.class);
                monitorQuery.setParameter("id", monitorId);
                Usuario monitor = monitorQuery.getSingleResult();

                // Crear la nueva actividad
                Actividad actividad = new Actividad(nombre, descripcion, monitor, precio);
                saveOrUpdate(actividad);

                response.sendRedirect(request.getContextPath() + "/catalogoActividades");

            } catch (Exception e) {
                request.setAttribute("msg", "Error al crear la actividad: " + e.getMessage());
                ViewUtils.vistaTemplate(request, response, usuario, "error");
            }
        } else if ("/vistaActividad".equals(action)) {
            // Verificar si se presiono el botón de agregar al carrito
            String agregarCarrito = request.getParameter("agregarCarrito");

            if (agregarCarrito != null) {
                // Si se desea agregar la actividad al carrito
                Long idActividad = Long.valueOf(request.getParameter("idActividad"));
                Actividad actividad = em.createNamedQuery("Actividad.findById", Actividad.class)
                        .setParameter("id", idActividad)
                        .getSingleResult();

                if (actividad != null) {
                    // Recuperar el carrito de la sesion, o crear uno vacio si no existe
                    List<Actividad> carrito = (List<Actividad>) request.getSession().getAttribute("carrito");
                    if (carrito == null) {
                        carrito = new ArrayList<>();
                    }

                    // Agregar la actividad al carrito
                    carrito.add(actividad);
                    // Guardar el carrito en la sesión
                    request.getSession().setAttribute("carrito", carrito);

                    response.sendRedirect(request.getContextPath() + "/catalogoActividades");
                    return;
                }
            }
        } else if ("/modificarActividad".equals(action)) {
            try {
                // Obtener los parámetros enviados desde el formulario
                Long id = Long.parseLong(request.getParameter("id"));
                String nombre = request.getParameter("nombre");
                String descripcion = request.getParameter("descripcion");
                Long monitorId = Long.parseLong(request.getParameter("monitor"));
                double precio = Double.parseDouble(request.getParameter("precio"));

                // Buscar la actividad existente por ID
                TypedQuery<Actividad> actividadQuery = em.createNamedQuery("Actividad.findById", Actividad.class)
                        .setParameter("id", id);
                Actividad actividad = actividadQuery.getSingleResult();

                if (actividad == null) {
                    request.setAttribute("msg", "La actividad no existe.");
                    response.sendRedirect(request.getContextPath() + "/catalogoActividades");
                    return;
                }

                // Buscar el monitor por ID
                TypedQuery<Usuario> monitorQuery = em.createNamedQuery("Usuario.findById", Usuario.class);
                monitorQuery.setParameter("id", monitorId);
                Usuario monitor = monitorQuery.getSingleResult();

                if (monitor == null) {
                    response.sendRedirect(request.getContextPath() + "/catalogoActividades");
                    return;
                }

                // Actualizar los datos de la actividad
                actividad.setNombre(nombre);
                actividad.setDescripcion(descripcion);
                actividad.setMonitor(monitor);
                actividad.setPrecio(precio);
                // Guardar los cambios en la base de datos
                saveOrUpdate(actividad);

                response.sendRedirect(request.getContextPath() + "/catalogoActividades");

            } catch (Exception e) {
                request.setAttribute("msg", "Error al modificar la actividad: " + e.getMessage());
                ViewUtils.vistaTemplate(request, response, usuario, "error");
            }
        } else if ("/eliminarActividad".equals(action)) {
            try {
                // Obtener el ID de la actividad a eliminar
                Long id = Long.parseLong(request.getParameter("id"));

                // Buscar la actividad
                TypedQuery<Actividad> actividadQuery = em.createNamedQuery("Actividad.findById", Actividad.class)
                        .setParameter("id", id);
                Actividad actividad = actividadQuery.getSingleResult();

                // Eliminar la actividad
                utx.begin();
                em.remove(em.merge(actividad));
                utx.commit();

                response.sendRedirect(request.getContextPath() + "/catalogoActividades");
            } catch (Exception e) {
                request.setAttribute("msg", "Error al eliminar la actividad: " + e.getMessage());
                ViewUtils.vistaTemplate(request, response, usuario, "error");
            }
        } else if ("/desapuntarseActividad".equals(action)) {
            try {
                // Obtener el ID de la actividad a eliminar
                Long idActividad = Long.parseLong(request.getParameter("idActividad"));

                // Eliminar usuario de la actividad
                eliminarUsuarioDeActividad(idActividad, usuario.getId());
                // Redirigir al catalogo con mensaje de éxito
                response.sendRedirect(request.getContextPath() + "/actividadesInscritas");
            } catch (Exception e) {
                request.setAttribute("msg", "Error al desapuntarse: " + e.getMessage());
                ViewUtils.vistaTemplate(request, response, usuario, "error");
            }
        }

    }

    public void saveOrUpdate(Actividad actividad) throws Exception {
        if (actividad.getId() == null) {
            utx.begin();
            em.persist(actividad);
            utx.commit();
        } else {
            utx.begin();
            em.merge(actividad);
            utx.commit();
        }
    }

    public void eliminarUsuarioDeActividad(Long actividadId, Long usuarioId) throws Exception {

        try {
            utx.begin();

            // Obtener la actividad
            Actividad actividad = em.find(Actividad.class, actividadId);
            if (actividad == null) {
                throw new Exception("Actividad no encontrada con ID: " + actividadId);
            }

            // Eliminar el usuario de la lista de usuarios de la actividad
            actividad.getUsuarios().removeIf(usuario -> usuario.getId().equals(usuarioId));

            // Sincronizar cambios
            em.merge(actividad);

            utx.commit();
        } catch (Exception e) {
            if (utx != null) {
                utx.rollback();
            }
            throw e;
        }
    }

}
