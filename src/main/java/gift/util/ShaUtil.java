package gift.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class ShaUtil {

    public static String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String encrypt(String text, String salt){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((text + salt).getBytes(StandardCharsets.UTF_8));
            byte[] pawSalt = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : pawSalt) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
