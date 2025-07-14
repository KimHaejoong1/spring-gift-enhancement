package gift.global.interceptor;

import gift.global.exception.CustomException;
import gift.global.exception.ErrorCode;
import gift.global.jwt.JwtUtil;
import gift.global.util.RequestAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final String BEARER_PREFIX = "Bearer ";

    public LoginInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN_REQUEST,
                ErrorCode.INVALID_TOKEN_REQUEST.getErrorMessage());
        }

        String token = header.substring(BEARER_PREFIX.length());

        // 원래 member 객체나 memberId등 controller에서 필요한 정보들을 httpServlet에 넣어두고,
        // controller에서 @RequestAttribute("")로 전달받아서 사용하려고 했습니다.
        // 참고로 토큰 자체에 대한 유효성 검증은 jwtUtil.getSubject()에서 수행됩니다.
        request.setAttribute(RequestAttributes.MEMBER_ID,
            Long.parseLong(jwtUtil.getSubject(token)));

        return true;
    }
}
