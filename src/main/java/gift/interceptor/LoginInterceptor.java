package gift.interceptor;

import gift.jwt.JwtService;
import gift.jwt.exception.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    public LoginInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            String token = extractTokenFromHeader(header);

            if (!jwtService.validateToken(token)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }

            Long memberId = jwtService.getMemberIdFromToken(token);
            request.setAttribute("memberId", memberId);
            request.setAttribute("accessToken", token);

            return true;
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }

    private String extractTokenFromHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new InvalidTokenException("유효하지 않은 Authorization 헤더");
    }
}
