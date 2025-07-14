package gift.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey getSecretKey() {
        return secretKey;
    }

    public String generateToken(Long memberId) {
        Date now = new Date();

        return Jwts.builder()
            .setSubject(memberId.toString())
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + 3600 * 1000))
            .signWith(secretKey)
            .compact();
    }


    public String getSubject(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        return claims.getSubject();
    }

}
