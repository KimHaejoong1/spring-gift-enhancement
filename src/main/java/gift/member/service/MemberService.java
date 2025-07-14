package gift.member.service;

import gift.jwt.JwtProvider;
import gift.member.dto.MemberRequestDto;
import gift.member.dto.MemberResponseDto;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository,
                         JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    //회원가입 기능
    public MemberResponseDto register(MemberRequestDto memberRequestDto){
        Long id = memberRepository.registerMember(memberRequestDto).getId();

        if(id == null){
            throw new RuntimeException("회원가입에 실패하였습니다");
        }

        Member member = new Member(
                id,
                memberRequestDto.getEmail(),
                memberRequestDto.getPassword(),
                memberRequestDto.getRole()
        );

        return new MemberResponseDto(jwtProvider.generateToken(member));

    }

    //로그인 기능
    public MemberResponseDto login(MemberRequestDto memberRequestDto){

        Member member = memberRepository.findMember(memberRequestDto).orElseThrow(
                () -> new RuntimeException("멤버를 찾지 못했습니다")
        );

        return new MemberResponseDto(jwtProvider.generateToken(member));

    }

    public Member findMemberById(Long id){
        return memberRepository.findMemberById(id).orElseThrow();
    }
}
