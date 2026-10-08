package util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class Util {

    // Hash a password with a random salt (PBKDF2, built into Java)
    public static String hash(String password) {
        try {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);
            return Base64.getEncoder().encodeToString(salt) + ":" + pbkdf2(password, salt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verify(String password, String stored) {
        try {
            String[] parts = stored.split(":");
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            return pbkdf2(password, salt).equals(parts[1]);
        } catch (Exception e) {
            return false;
        }
    }

    private static String pbkdf2(String password, byte[] salt) throws Exception {
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] h = f.generateSecret(new PBEKeySpec(password.toCharArray(), salt, 65536, 256)).getEncoded();
        return Base64.getEncoder().encodeToString(h);
    }

    // Escape text before printing in HTML (prevents XSS)
    public static String esc(Object o) {
        if (o == null) return "";
        return o.toString().replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }
}
