package gift.resolver;

import gift.dto.member.MemberCredentialDto;
import gift.entity.LoginMember;
import gift.entity.Member;
import gift.exception.UnAuthenicatedException;
import gift.service.member.MemberService;
import gift.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public LoginMemberArgumentResolver(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request
            = (HttpServletRequest) webRequest.getNativeRequest();

        String authenicatedHeader = request.getHeader("Authorization");

        if (authenicatedHeader == null || !authenicatedHeader.startsWith("Bearer ")) {
            throw new UnAuthenicatedException("요청 헤더에 Authorization이 없습니다.");
        }

        String token = authenicatedHeader.substring(7);
        Long memberId = jwtUtil.getMemberIdFromToken(token);
        MemberCredentialDto responseDto = memberService.findById(memberId);

        return new Member(responseDto.id(), responseDto.email(), responseDto.password());
    }
}
