package gift.service;

import gift.domain.Member;
import gift.exception.ForbiddenException;
import gift.repository.MemberRepository;
import gift.auth.JwtTokenProvider;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MessageSource messageSource;

    public MemberService(MemberRepository memberRepository,
                         PasswordEncoder passwordEncoder,
                         JwtTokenProvider jwtTokenProvider,
                         MessageSource messageSource) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.messageSource = messageSource;
    }

    // 회원가입
    public String register(String email, String rawPassword) {
        // 유효성 검사만 먼저 수행
        Member.validateForRegister(email, rawPassword);

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 암호화된 비밀번호로 Member 객체 생성
        Member member = Member.withEncodedPassword(email, encodedPassword);
        Member saved = memberRepository.save(member);

        return jwtTokenProvider.createToken(saved.getId());
    }

    // 로그인
    public String login(String email, String password) {
        // 1. 이메일로 회원 찾기
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ForbiddenException(getMessage("member.login.failed")));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new ForbiddenException(getMessage("member.login.failed"));
        }

        // 3. JWT 토큰 생성 및 반환
        return jwtTokenProvider.createToken(member.getId());
    }

    // ID로 회원 조회
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault());
    }
}