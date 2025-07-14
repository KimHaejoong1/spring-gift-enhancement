package gift.member.repository;

import gift.member.dto.MemberRequestDto;
import gift.member.dto.MemberResponseDto;
import gift.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    void saveMember(Member member);
    Optional<Member> findByEmail(String email);
    List<MemberResponseDto> getAllMembers();
    Optional<Member> getMemberById(Long id);
    void updateMember(Long id, MemberRequestDto memberRequestDto);
    void deleteMember(Long id);
}
