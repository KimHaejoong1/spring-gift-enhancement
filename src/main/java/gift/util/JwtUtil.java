package gift.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.token-validity}")
    private long tokenTime;

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(Long id, String email) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + tokenTime);

        return Jwts.builder()
            .setSubject(email)
            .claim("id", id)
            .claim("email", email)
            .setIssuedAt(now)
            .setExpiration(expiredDate)
            .signWith(key)
            .compact();
    }

    public Long getMemberIdFromToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        return claims.get("id", Long.class);
    }
}