package daw.controllers;

import daw.model.Usuario;
import daw.util.ViewUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


@WebFilter(filterName = "filtro", urlPatterns = {"/*"})
public class filtro implements Filter {

    private static final boolean debug = true;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured.
    private FilterConfig filterConfig = null;

    private ArrayList<String> logoutPermitedList = new ArrayList<>();
    private ArrayList<String> usuariosPermitedList = new ArrayList<>();
    private ArrayList<String> personalPermitedList = new ArrayList<>();

    public filtro() {

        logoutPermitedList.add("/login");
        logoutPermitedList.add("/register");
        logoutPermitedList.add("/catalogoActividades");
        logoutPermitedList.add("/vistaActividad");

        usuariosPermitedList.add("/catalogoActividades");
        usuariosPermitedList.add("/logout");
        usuariosPermitedList.add("/carrito");
        usuariosPermitedList.add("/agregarCarrito");
        usuariosPermitedList.add("/eliminarCarrito");
        usuariosPermitedList.add("/inscribirseCarrito");
        usuariosPermitedList.add("/actividadesInscritas");
        usuariosPermitedList.add("/vistaActividad");
        usuariosPermitedList.add("/desapuntarseActividad");
        usuariosPermitedList.add("/miCuenta");

        personalPermitedList = (ArrayList<String>) usuariosPermitedList.clone();
        personalPermitedList.add("/borrarUsuario");
        personalPermitedList.add("/crearActividad");
        personalPermitedList.add("/modificarActividad");
        personalPermitedList.add("/eliminarActividad");
        personalPermitedList.add("/cuentasUsuarios");
        personalPermitedList.add("/modificarUsuario");
        personalPermitedList.add("/borrarUsuario");
        personalPermitedList.add("/confirmarBorrarUsuario");
        personalPermitedList.add("/registerUser");

        System.out.println("ListaLogOut: " + logoutPermitedList);
        System.out.println("ListaUsuarios: " + usuariosPermitedList);
        System.out.println("ListaPersonal: " + personalPermitedList);

    }

    /*
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Usuario usuario = (Usuario) httpRequest.getSession().getAttribute("usuario");

        String path = httpRequest.getRequestURI();  // Obtiene la URI completa de la solicitud

        // Si la solicitud es para archivos estáticos como CSS, JS o imágenes, no filtramos
        if (path.startsWith(httpRequest.getContextPath() + "/css/")
                || path.startsWith(httpRequest.getContextPath() + "/js/")
                || path.startsWith(httpRequest.getContextPath() + "/resources/images/")
                || path.startsWith(httpRequest.getContextPath() + "/resources/photos/")) {
            chain.doFilter(request, response);
            return;  // Termina aquí para evitar que se siga procesando
        }

        // si la solicitud es la raíz del sitio "/", redirigir a "/catalogoActividades"
        if (path.equals(httpRequest.getContextPath() + "/")) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/catalogoActividades");
            return;  // Detener la ejecución del filtro y redirigir al cliente
        }

        String action = httpRequest.getServletPath();
       // String template;
        boolean error = false;

        if (usuario == null) {
           // template = "templateLogOut";

            if (logoutPermitedList.contains(action)) {
                //Enviamos al servlet
                chain.doFilter(request, response);
            } else {
                error = true;
            }
        } else if (usuario.getTipo().equals("Usuario")) {
         //   template = "templateUsuarios";

            if (usuariosPermitedList.contains(action)) {
                //Enviamos al servlet
                chain.doFilter(request, response);
            } else {
                error = true;
            }
        } else if (usuario.getTipo().equals("Admin")) {
         //   template = "templatePersonal";

            if (personalPermitedList.contains(action)) {
                //Enviamos al servlet
                chain.doFilter(request, response);
            } else {
                error = true;
            }
        } else {
            error = true;
        }

        if (error) {
            request.setAttribute("msg", "Error, página no encontrada o sin acceso");
            ViewUtils.vistaTemplate(httpRequest, httpResponse, usuario, "error");
            //System.out.println("Error");

        }

    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
            }
        }
    }

}
