package gift.config;

import gift.service.MemberService;
import gift.security.JwtTokenProvider;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public LoginMemberArgumentResolver(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
         String authorizationHeader = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

         if (authorizationHeader == null || !authorizationHeader.startsWith(AUTHORIZATION_PREFIX)) {
             throw new AuthenticationException("Invalid Authorization header");
         }

         String token = authorizationHeader.substring(AUTHORIZATION_PREFIX.length());
        long memberId = jwtTokenProvider.parseJwtToken(token).getId();

        return memberService.getMember(memberId);
    }
}
