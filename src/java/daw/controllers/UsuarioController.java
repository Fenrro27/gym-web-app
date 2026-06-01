package daw.controllers;

import daw.model.Usuario;
import daw.util.FormUtils;
import daw.util.PasswordUtils;
import daw.util.ViewUtils;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.transaction.UserTransaction;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;


@WebServlet(name = "UsuarioController", urlPatterns = {"/miCuenta", "/cuentasUsuarios", "/modificarUsuario", "/borrarUsuario", "/confirmarBorrarUsuario"})
@MultipartConfig
public class UsuarioController extends HttpServlet {

    @PersistenceContext(unitName = "AgendaPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;

 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //para evitar que la cache altere los resultados de las peticiones
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        String path = request.getServletPath();
        if ("/cuentasUsuarios".equals(path)) {
            try {
                TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findAllExcept", Usuario.class);
                query.setParameter("id", usuario.getId());
                List<Usuario> usuariosAll = query.getResultList();

                request.setAttribute("usuario", usuariosAll);
                ViewUtils.vistaTemplate(request, response, usuario, "cuentasUsuarios");
            } catch (Exception ex) {
                request.setAttribute("msg", "Error: " + ex.getMessage());
                ViewUtils.vistaTemplate(request, response, usuario, "error");
            }

        } else if ("/miCuenta".equals(path)) {

            request.setAttribute("usuario", usuario);
            ViewUtils.vistaTemplate(request, response, usuario, "miCuenta");

        } else if ("/modificarUsuario".equals(path)) {
            try {
                String idParam = request.getParameter("id");
                Long id = Long.parseLong(idParam);
                TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findById", Usuario.class)
                        .setParameter("id", id);
                Usuario usuarioMod = query.getSingleResult();

                request.setAttribute("usuario", usuarioMod);
                ViewUtils.vistaTemplate(request, response, usuario, "modificarCuenta");
            } catch (Exception ex) {
                request.setAttribute("msg", "Error: " + ex.getMessage());
                ViewUtils.vistaTemplate(request, response, usuario, "error");
            }
        } else if ("/confirmarBorrarUsuario".equals(path)) {
            // Recuperar los parámetros de nombre, correo e id del usuario
            try {
                String id = request.getParameter("id");
                String nombre = request.getParameter("nombre");
                String email = request.getParameter("email");

                TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findById", Usuario.class);
                query.setParameter("id", Long.parseLong(id));
                Usuario usuarioAEliminar = query.getSingleResult();

                // Verificar si el usuario está a cargo de alguna actividad antes de eliminarlo
                if (usuarioAEliminar != null && !permitirBorrar(usuarioAEliminar)) {

                    TypedQuery<Usuario> query2 = em.createNamedQuery("Usuario.findAllExcept", Usuario.class);
                    query2.setParameter("id", usuario.getId());
                    List<Usuario> usuariosAll = query2.getResultList();

                    request.setAttribute("usuario", usuariosAll);
                    request.setAttribute("msg", "Atención, no se permite eliminar al usuario " + email);
                    ViewUtils.vistaTemplate(request, response, usuario, "cuentasUsuarios");

                    return;
                }

                // Pasamos los datos a la vista de confirmación
                request.setAttribute("nombre", nombre);
                request.setAttribute("email", email);
                request.setAttribute("id", id);
                ViewUtils.vistaTemplate(request, response, usuario, "Usuario/confirmarBorrarUsuario");

            } catch (Exception ex) {
                request.setAttribute("msg", "Error: " + ex.getMessage());
                ViewUtils.vistaTemplate(request, response, usuario, "error");
            }
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuarioLogeado = (Usuario) request.getSession().getAttribute("usuario");
        String path = request.getServletPath();

        if ("/miCuenta".equals(path)) {
            String nombre = request.getParameter("name");
            String dni = request.getParameter("dni");
            String email = request.getParameter("email");
            String telefono = request.getParameter("phone");
            String fechanacimiento = request.getParameter("fechanacimiento");
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmNewPassword = request.getParameter("confirmNewPassword");

            try {

                // Verificar contraseña actual
                String hashedCurrentPassword = PasswordUtils.hashPasswordMD5(currentPassword);

                if (!usuarioLogeado.getContrasenna().equals(hashedCurrentPassword)) {
                    throw new Exception("La contraseña actual es incorrecta.");
                }

                // Validaciones
                if (fechanacimiento == null) {
                    throw new Exception("Tiene que haber una fecha de nacimiento.");
                }
                if (nombre == null || nombre.isEmpty()) {
                    throw new Exception("El nombre no puede estar vacio.");
                }

                if (!FormUtils.isValidDNI(dni)) {
                    throw new Exception("El DNI no es valido. Debe tener el formato correcto (por ejemplo: 12345678A).");
                }
                if (!FormUtils.isValidEmail(email)) {
                    throw new Exception("El email no es valido.");
                }
                if (!FormUtils.isValidPhone(telefono)) {
                    throw new Exception("El numero de telefono no es valido. Debe contener solo digitos y tener entre 9 y 15 caracteres.");
                }

                // Actualizar datos basicos
                usuarioLogeado.setName(nombre);
                usuarioLogeado.setDNI(dni);
                usuarioLogeado.setEmail(email);
                usuarioLogeado.setTelefono(Integer.parseInt(telefono));

                // Actualizar fecha de nacimiento
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                usuarioLogeado.setFechaNacimiento(dateFormat.parse(fechanacimiento));

                // Verificar y actualizar nueva contraseña
                if (newPassword != null && !newPassword.isEmpty()) {
                    if (!newPassword.equals(confirmNewPassword)) {
                        throw new Exception("Las nuevas contraseñas no coinciden.");
                    }
                    usuarioLogeado.setContrasenna(PasswordUtils.hashPasswordMD5(newPassword));
                }

                // Guardar cambios
                saveorUpdate(usuarioLogeado);

                //Guardamos o actualizamos la foto
                final Part imgPart = request.getPart("photo");
                //obtenemos la direccion de guardado
                String absolutePathFolder = getServletContext().getRealPath("" + File.separator + "resources" + File.separator + "photos");
                FormUtils.savePhoto(absolutePathFolder, usuarioLogeado, imgPart);

                request.setAttribute("msg", "Datos actualizados exitosamente.");

            } catch (Exception e) {
                request.setAttribute("msg", "Error: " + e.getMessage());
            }

            request.setAttribute("usuario", usuarioLogeado);
            ViewUtils.vistaTemplate(request, response, usuarioLogeado, "miCuenta");

        } else if ("/modificarUsuario".equals(path)) {

            Usuario usuarioAModificar = null;
            try {
                // Validar acceso del administrador con su contraseña
                String adminPassword = request.getParameter("adminPassword");
                String hashedAdminPassword = PasswordUtils.hashPasswordMD5(adminPassword);

                if (!usuarioLogeado.getContrasenna().equals(hashedAdminPassword)) {
                    throw new Exception("La contraseña del administrador es incorrecta.");
                }

                Long id = Long.parseLong(request.getParameter("id"));
                TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findById", Usuario.class);
                query.setParameter("id", id);
                usuarioAModificar = query.getSingleResult();

                if (usuarioAModificar == null) {
                    throw new Exception("Usuario no encontrado.");
                }

                String nombre = request.getParameter("name");
                String dni = request.getParameter("dni");
                String email = request.getParameter("email");
                String telefono = request.getParameter("phone");
                String fechanacimiento = request.getParameter("fechanacimiento");
                String newPassword = request.getParameter("newPassword");
                String confirmNewPassword = request.getParameter("confirmNewPassword");

                // Validaciones
                if (fechanacimiento == null) {
                    throw new Exception("Tiene que haber una fecha de nacimiento.");
                }
                if (nombre == null || nombre.isEmpty()) {
                    throw new Exception("El nombre no puede estar vacio.");
                }

                if (!FormUtils.isValidDNI(dni)) {
                    throw new Exception("El DNI no es valido. Debe tener el formato correcto (por ejemplo: 12345678A).");
                }
                if (!FormUtils.isValidEmail(email)) {
                    throw new Exception("El email no es valido.");
                }
                if (!FormUtils.isValidPhone(telefono)) {
                    throw new Exception("El numero de telefono no es valido. Debe contener solo digitos y tener entre 9 y 15 caracteres.");
                }

                usuarioAModificar.setName(nombre);
                usuarioAModificar.setDNI(dni);
                usuarioAModificar.setEmail(email);
                usuarioAModificar.setTelefono(Integer.parseInt(telefono));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                usuarioAModificar.setFechaNacimiento(dateFormat.parse(fechanacimiento));

                if (newPassword != null && !newPassword.isEmpty()) {
                    if (!newPassword.equals(confirmNewPassword)) {
                        throw new Exception("Las nuevas contraseñas no coinciden.");
                    }
                    usuarioAModificar.setContrasenna(PasswordUtils.hashPasswordMD5(newPassword));
                }

                saveorUpdate(usuarioAModificar);

                //Guardamos o actualizamos la foto
                final Part imgPart = request.getPart("photo");
                //obtenemos la direccion de guardado
                String absolutePathFolder = getServletContext().getRealPath("" + File.separator + "resources" + File.separator + "photos");
                FormUtils.savePhoto(absolutePathFolder, usuarioAModificar, imgPart);

                request.setAttribute("msg", "Usuario actualizado exitosamente.");

            } catch (Exception e) {
                try {
                    Long id = Long.parseLong(request.getParameter("id"));
                    TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findById", Usuario.class);
                    query.setParameter("id", id);
                    Usuario usuarioModificado = query.getSingleResult();

                    request.setAttribute("usuario", usuarioModificado);
                    request.setAttribute("msg", "Error: " + e.getMessage());
                    ViewUtils.vistaTemplate(request, response, usuarioLogeado, "modificarCuenta");

                } catch (Exception e2) {
                    request.setAttribute("msg", "Error: " + e2.getMessage());
                    ViewUtils.vistaTemplate(request, response, usuarioLogeado, "error");
                }
            }

            response.sendRedirect(request.getContextPath() + "/cuentasUsuarios");

        } else if ("/borrarUsuario".equals(path)) {
            // Obtener el ID del usuario a eliminar
            String id = request.getParameter("id");

            // Buscar el usuario por ID
            TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findById", Usuario.class);
            query.setParameter("id", Long.parseLong(id));
            Usuario usuarioAEliminar = query.getSingleResult();

            // Verificar si el usuario esta a cargo de alguna actividad antes de eliminarlo
            if (usuarioAEliminar != null && !permitirBorrar(usuarioAEliminar)) {
                response.sendRedirect(request.getContextPath() + "/cuentasUsuarios");
                return;
            }

            // Si el usuario puede ser eliminado, continuar con la eliminacion
            try {
                utx.begin(); // Inicia la transaccion
                Query queryDelete = em.createNamedQuery("Usuario.deleteById");
                queryDelete.setParameter("id", Long.parseLong(id)); 

                // Ejecutar la consulta de eliminacion
                int rowsAffected = queryDelete.executeUpdate();

                if (rowsAffected > 0) {
                    // Confirmar la transacción
                    utx.commit();

                    // Redirigir a la vista de usuarios después de eliminar
                    response.sendRedirect(request.getContextPath() + "/cuentasUsuarios");
                } else {
                    utx.rollback();
                    request.setAttribute("msg", "Usuario no encontrado.");
                    ViewUtils.vistaTemplate(request, response, usuarioLogeado, "cuentasUsuarios");
                }
            } catch (Exception e) {
                // Manejo de excepciones
                if (utx != null) {
                    try {
                        utx.rollback(); // Hacer rollback en caso de error
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                e.printStackTrace();
                request.setAttribute("msg", "Error al eliminar el usuario.");
                ViewUtils.vistaTemplate(request, response, usuarioLogeado, "cuentasUsuarios");

            }
        }
    }

  
    private void saveorUpdate(Usuario usuario) throws Exception {
        if (usuario.getId() == null) {
            utx.begin();
            em.persist(usuario);
            utx.commit();
        } else {
            utx.begin();
            em.merge(usuario);
            utx.commit();
        }

    }

    public boolean permitirBorrar(Usuario usuario) {

        List<Usuario> monitores = em.createNamedQuery("Actividad.findMonitores", Usuario.class)
                .getResultList();

        return !monitores.contains(usuario);  // Devuelve true si no está en la lista de monitores, false si está
    }

}
