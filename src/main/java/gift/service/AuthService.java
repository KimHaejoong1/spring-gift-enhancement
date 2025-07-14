package gift.service;

import gift.dto.AuthResponseDto;
import gift.dto.MemberRequestDto;
import gift.domain.Member;
import gift.security.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponseDto register(MemberRequestDto memberRequestDto) {
        Member member = memberService.creatMember(memberRequestDto);
        String token = jwtTokenProvider.makeJwtToken(member.getId());
        return new AuthResponseDto(token);
    }

    public AuthResponseDto login(MemberRequestDto memberRequestDto) {
        Member member = memberService.login(memberRequestDto);
        String token = jwtTokenProvider.makeJwtToken(member.getId());
        return new AuthResponseDto(token);
    }
}
