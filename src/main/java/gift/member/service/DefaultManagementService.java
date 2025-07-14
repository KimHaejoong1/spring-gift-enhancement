package gift.member.service;

import gift.member.dto.MemberRequestDto;
import gift.member.dto.MemberResponseDto;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import gift.common.security.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultManagementService implements MemberManagementService {

    private final MemberRepository memberRepository;

    public DefaultManagementService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public List<MemberResponseDto> getAllMembers() {
        return memberRepository.getAllMembers();
    }

    @Override
    public MemberResponseDto addMember(MemberRequestDto dto) {

        String encodedPassword = PasswordUtil.sha256(dto.password());
        Member member = new Member(null, dto.email(), encodedPassword, dto.role());
        memberRepository.saveMember(member);

        Member saved = memberRepository.findByEmail(dto.email())
                .orElseThrow(() -> new IllegalStateException("회원 저장 실패"));

        return new MemberResponseDto(saved);
    }

    @Override
    public MemberResponseDto updateMember(Long id, MemberRequestDto memberRequestDto) {
        memberRepository.updateMember(id, memberRequestDto);

        return memberRepository.getMemberById(id)
                .map(MemberResponseDto::new)
                .orElseThrow(() -> new RuntimeException("회원 수정 실패: ID " + id));
    }

    @Override
    public MemberResponseDto deleteMember(Long id) {
        Member member = memberRepository.getMemberById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id=" + id));

        memberRepository.deleteMember(id);

        return new MemberResponseDto(member);
    }

}
