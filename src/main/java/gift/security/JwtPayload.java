package gift.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

public class JwtPayload {

    private final Long id;

    public JwtPayload(Claims claims) {
        String idString = claims.get("sub", String.class);

        if (idString.isEmpty()) {
            throw new JwtException("NOT valid Token");
        }

        id = Long.parseLong(idString);
    }

    public Long getId() {
        return id;
    }
}
