package gift.login;

import gift.domain.Member;
import gift.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.NoSuchElementException;


@Component
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

    public LoginArgumentResolver(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String userEmail = (String) request.getAttribute("userEmail");

        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> new NoSuchElementException("등록되지 않은 사용자입니다."));
        return new LoginMember(member.getId(), member.getEmail());
    }
}
