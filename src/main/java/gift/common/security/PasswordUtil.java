package gift.common.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtil {

    // SHA-256 해싱 + Base64 인코딩
    public static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes); // Base64로 문자열 변환
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // 사용 예시 (비교는 직접 문자열 비교)
    public static boolean matches(String rawPassword, String hashedPassword) {
        return sha256(rawPassword).equals(hashedPassword);
    }
}
