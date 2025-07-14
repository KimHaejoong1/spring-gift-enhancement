package gift.api.member.service;

import gift.api.member.domain.Member;
import gift.api.member.domain.MemberRole;
import gift.api.member.dto.MemberRequestDto;
import gift.api.member.dto.TokenResponseDto;
import gift.api.member.repository.MemberRepository;
import gift.exception.LoginFailedException;
import gift.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public TokenResponseDto registerMember(MemberRequestDto memberRequestDto) {
        memberRepository.findByEmail(memberRequestDto.email()).ifPresent(member -> {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        });

        String encodedPassword = BCrypt.hashpw(memberRequestDto.password(), BCrypt.gensalt());
        Member member = new Member(
                null,
                memberRequestDto.email(),
                encodedPassword,
                MemberRole.USER
        );
        memberRepository.registerMember(member);

        String token = jwtUtil.createToken(memberRequestDto.email(), MemberRole.USER);

        return new TokenResponseDto(token);
    }

    public TokenResponseDto loginMember(MemberRequestDto memberRequestDto) {
        Member member = memberRepository.findByEmail(memberRequestDto.email())
                .orElseThrow(() -> new LoginFailedException("이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!BCrypt.checkpw(memberRequestDto.password(), member.getPassword())) {
            throw new LoginFailedException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createToken(member.getEmail(), member.getRole());

        return new TokenResponseDto(token);
    }
}
