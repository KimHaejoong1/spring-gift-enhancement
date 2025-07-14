package gift.member.service;

import gift.member.dto.MemberRequestDto;
import gift.member.dto.TokenResponseDto;
import gift.member.entity.Member;
import gift.common.exception.DuplicateEmailException;
import gift.common.exception.LoginFailedException;
import gift.member.repository.MemberRepository;
import gift.common.security.JwtUtil;
import gift.common.security.PasswordUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements AuthService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public AuthenticationService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public TokenResponseDto signUp(MemberRequestDto requestDto) {
        // 이메일 중복 체크
        if (memberRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new DuplicateEmailException(requestDto.email());
        }

        // 비밀번호 암호화
        String encodedPassword = PasswordUtil.sha256(requestDto.password());

        // 회원 저장
        Member member = new Member(requestDto.email(), encodedPassword, requestDto.role());
        memberRepository.saveMember(member);

        // 토큰 생성
        String token = jwtUtil.generateToken(member.getEmail(), member.getId(), member.getRole().name());
        return new TokenResponseDto(token);
    }

    @Override
    public TokenResponseDto login(MemberRequestDto requestDto) {
        // 이메일 확인
        Member member = memberRepository.findByEmail(requestDto.email())
                .orElseThrow(LoginFailedException::new);

        // 비밀번호 확인
        if (!PasswordUtil.matches(requestDto.password(), member.getPassword())) {
            throw new LoginFailedException();
        }

        // 토큰 생성
        String token = jwtUtil.generateToken(member.getEmail(), member.getId(), member.getRole().name());
        return new TokenResponseDto(token);
    }
}
