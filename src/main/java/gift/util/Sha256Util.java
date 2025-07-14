package gift.util;

import gift.exception.UnsupportedShaAlgorithmException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Component;

@Component
public class Sha256Util {
    public String encrypt(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(text.getBytes());

            return bytesToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedShaAlgorithmException("SHA-256 알고리즘을 지원하지 않습니다.", e);
        }
    }

    public String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }

        return builder.toString();
    }

    public String encryptWithAlgorithm(String text, String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(text.getBytes());

            return bytesToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedShaAlgorithmException("SHA-256 알고리즘을 지원하지 않습니다.", e);
        }
    }
}
