package gift.auth.jwt;

import gift.common.code.CustomResponseCode;
import gift.common.exception.CustomException;
import gift.entity.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class JwtFilter implements Filter {

    private final JwtProvider jwtProvider;

    private static final Set<String> EXCLUDED_PATHS = Set.of(
        "/api/auth/register",
        "/api/auth/login",
        "/h2-console"
    );

    public JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            String path = httpRequest.getRequestURI();

            if (EXCLUDED_PATHS.contains(path)) {
                chain.doFilter(request, response);
                return;
            }

            String token = jwtProvider.extractToken(httpRequest);
            Map<String, Object> claims = jwtProvider.getClaimsFromToken(token);
            Long userId = ((Number) claims.get("userId")).longValue();
            String email = (String) claims.get("sub");

            User user = new User(userId, email, null);
            httpRequest.setAttribute("user", user);

            chain.doFilter(request, response);

        } catch (CustomException e) {
            sendErrorResponse(httpResponse, e.getErrorCode());
        }
    }

    private void sendErrorResponse(HttpServletResponse response, CustomResponseCode code)
        throws IOException {
        response.setStatus(code.getCode());
        response.setContentType("application/json; charset=UTF-8");

        String jsonResponse = String.format("""
            {
              "status": %d,
              "message": "%s",
              "data": null
            }
            """, code.getCode(), code.getMessage());

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
