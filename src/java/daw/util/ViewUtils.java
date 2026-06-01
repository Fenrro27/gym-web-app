
package daw.util;

import daw.model.Usuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ViewUtils {
    
    
      public static void vistaTemplate(HttpServletRequest request, HttpServletResponse response, Usuario usuarioLog, String vista) throws ServletException, IOException {

        String vistaTemplate;
        if (usuarioLog == null) {
            vistaTemplate = "templateLogOut";
        } else if ("Admin".equals(usuarioLog.getTipo())) {
            vistaTemplate = "templatePersonal"; // Template para admin
        } else {
            vistaTemplate = "templateUsuarios"; // Template para usuarios regulares
        }

        request.setAttribute("vista", vista);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/" + vistaTemplate + ".jsp");
        rd.forward(request, response);
    }
    
}
