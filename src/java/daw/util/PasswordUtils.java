
package daw.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class PasswordUtils {
     public static String hashPasswordMD5(String password) throws NoSuchAlgorithmException {
        // Crear una instancia de MessageDigest para MD5
        MessageDigest md = MessageDigest.getInstance("MD5");

        // Aplicar el hash a la contraseña
        byte[] hashBytes = md.digest(password.getBytes());

        // Convertir el hash a una cadena hexadecimal
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
