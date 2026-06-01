
package daw.util;

import daw.model.Usuario;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;


public class FormUtils {

    public static boolean isValidDNI(String dni) {
        // Patroo para DNI español (8 digitos seguidos de una letra)
        String dniPattern = "^[0-9]{8}[A-Za-z]$";
        return Pattern.matches(dniPattern, dni);
    }

    public static boolean isValidEmail(String email) {
        // Patron para email basico
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(emailPattern, email);
    }

    public static boolean isValidPhone(String phone) {
        // Patron para numeros de telefono (solo digitos, entre 9 y 15 caracteres)
        String phonePattern = "^[0-9]{9,15}$";
        return Pattern.matches(phonePattern, phone);
    }

    public static boolean savePhoto( String absolutePathFolder, Usuario usuario, Part imgPart) throws FileNotFoundException, IOException {
        if (imgPart != null) {

            String fileName = usuario.getId().toString();
            File f = new File(absolutePathFolder + File.separator + "US" + fileName + ".jpg");//Direccion y nombre de guardado
            OutputStream fos = new FileOutputStream(f);//escribir
            InputStream filecontent = imgPart.getInputStream();//leer
            int read = 0;
            final byte[] bytes = new byte[1024];//Leemos y copiamos byte a byte
            while ((read = filecontent.read(bytes)) != -1) {
                fos.write(bytes, 0, read);
            }

            fos.close();
            filecontent.close();
            return true;
        } else {
            return false;
        }

    }

}
