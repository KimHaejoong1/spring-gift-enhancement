package gift.member.dto;

import gift.member.entity.Member;
import gift.member.entity.Role;

public record MemberResponseDto(Long id, String email, String password, Role role) {
    public MemberResponseDto(Member member) {
        this(member.getId(), member.getEmail(), member.getPassword(), member.getRole());
    }
}
