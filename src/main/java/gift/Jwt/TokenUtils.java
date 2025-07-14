package gift.Jwt;

import gift.entity.UserRole;
import gift.exception.userException.NeedAuthorizedException;
import gift.exception.userException.UnauthorizedException;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class TokenUtils {

    private final JwtUtil jwtUtil;

    public TokenUtils(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String extractToken(String authHeader) {
        return authHeader.replace("Bearer ", "").trim();
    }

    public void validateToken(String token) {
        if (!jwtUtil.validate(token)) {
            throw new UnauthorizedException();
        }
    }

    public String extractEmail(String token) {
        Claims claims = jwtUtil.getClaims(token);
        return claims.get("email", String.class);
    }

    private static final String CLAIM_ROLE = "role";

    public boolean hasRole(String token, UserRole expectedRole) {
        Claims claims = jwtUtil.getClaims(token);
        String roleStr = claims.get(CLAIM_ROLE, String.class);

        try {
            UserRole role = UserRole.valueOf(roleStr);
            return expectedRole == role;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }


    public boolean requireAdmin(String token) {
        if (!hasRole(token, UserRole.ADMIN)) {
            throw new NeedAuthorizedException();
        }
        return true;
    }

    public Claims getClaims(String token) {
        return jwtUtil.getClaims(token);
    }
}
