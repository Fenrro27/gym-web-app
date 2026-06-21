package daw.controllers;

import daw.util.PasswordUtils;
import daw.model.Usuario;
import daw.util.FormUtils;
import daw.util.ViewUtils;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
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
import java.util.Date;

@WebServlet(name = "LoginController", urlPatterns = {"/login", "/register", "/registerUser", "/logout"})
@MultipartConfig
public class LoginController extends HttpServlet {

    @PersistenceContext(unitName = "GymPU")
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
        String vista = "error";
        String accion = request.getServletPath();

        if ("/login".equals(accion)) {
            vista = "formLogin";  // Vista para el formulario de inicio de sesión
        } else if ("/register".equals(accion)) {
            vista = "formRegistroUsuarios";
        } else if ("/registerUser".equals(accion)) {
            vista = "formRegistroPersonal";
        } else if ("/logout".equals(accion)) {
            // Invalidar la sesión actual
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        ViewUtils.vistaTemplate(request, response, usuario, vista);
      
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getServletPath();

        if ("/login".equals(accion)) {
            postLogin(request, response);
        } else if ("/register".equals(accion)) {
            postRegister(request, response);
        } else if ("/registerUser".equals(accion)) {
            postRegister(request, response);
        }

    }

    private void postLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            // Hashear la contraseña ingresada
            String hashedPassword = PasswordUtils.hashPasswordMD5(password);

//            System.out.println("hashedPS: " + hashedPassword);
            Usuario usuario = null;
            try {
                usuario = em.createNamedQuery("Usuario.findByEmailAndPassword", Usuario.class)
                        .setParameter("email", email)
                        .setParameter("contrasenna", hashedPassword)
                        .getSingleResult();

            } catch (NoResultException e) {
                request.setAttribute("msg", "Error: Usuario o contraseña incorrectos.");
                ViewUtils.vistaTemplate(request, response, usuario, "formLogin");
            }

            if (usuario != null) {
                // Usuario encontrado, iniciamos sesión
                // Crear sesión para el usuario
                request.getSession().setAttribute("usuario", usuario);

                // Redirigir a la página deseada después del inicio de sesión
                response.sendRedirect(request.getContextPath() + "/catalogoActividades");
            }
        } catch (Exception e) {
            request.setAttribute("msg", "Error al procesar el inicio de sesión: " + e.getMessage());
            Usuario us = (Usuario) request.getSession().getAttribute("usuario");
            ViewUtils.vistaTemplate(request, response, us, "error");

        }
    }

    private void postRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String nombre = request.getParameter("name");
            String dni = request.getParameter("dni");
            String email = request.getParameter("email");
            String telefono = request.getParameter("phone");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String role = request.getParameter("role"); // Obtenemos el rol seleccionado en el formulario

            Date fechaNacimiento = null;
            Date fechaEntrada = new Date(); // Fecha actual como fecha de entrada

            // Parseamos la fecha de nacimiento
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            fechaNacimiento = sdf.parse(request.getParameter("fechanacimiento"));

            // Validaciones
            if (fechaNacimiento == null) {
                throw new Exception("Tiene que haber una fecha de nacimiento.");
            }
            if (nombre == null || nombre.isEmpty()) {
                throw new Exception("El nombre no puede estar vacio.");
            }
            if (password == null || password.isEmpty()) {
                throw new Exception("La contraseña no puede estar vacia.");
            }
            if (confirmPassword == null || confirmPassword.isEmpty()) {
                throw new Exception("La confirmacion de la contraseña no puede estar vacia.");
            }
            if (!password.equals(confirmPassword)) {
                throw new Exception("Las contraseñas no coinciden");
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

            // Forzar el rol a "Usuario" si el usuario actual no es Admin
            Usuario usuarioSesion = (Usuario) request.getSession().getAttribute("usuario");
            if (usuarioSesion == null || !"Admin".equals(usuarioSesion.getTipo())) {
                role = "Usuario"; // Asignar "Usuario" si el usuario en sesión no es Admin
            }

            // Hashear la contraseña antes de guardarla
            String hashedPassword = PasswordUtils.hashPasswordMD5(password);

            Usuario usuario = new Usuario(nombre, dni, fechaNacimiento, Integer.parseInt(telefono), email, fechaEntrada, hashedPassword, role);
            save(usuario); // Guardamos el usuario en la base de datos

            //Guardamos la foto
            final Part imgPart = request.getPart("photo");
            //obtenemos la direccion de guardado
            String absolutePathFolder = getServletContext().getRealPath("" + File.separator + "resources" + File.separator + "photos");
            FormUtils.savePhoto(absolutePathFolder, usuario, imgPart);

            response.sendRedirect(request.getContextPath() + "/catalogoActividades");

        } catch (Exception e) {
            Usuario us = (Usuario) request.getSession().getAttribute("usuario");
            request.setAttribute("msg", "Error: " + e.getMessage());
            ViewUtils.vistaTemplate(request, response, us, "error");
        }
    }

    public void save(Usuario usuario) throws Exception {
        try {
            utx.begin();
            em.persist(usuario);
            utx.commit();
        } catch (Exception e) {
            throw e;
        }
    }
}
