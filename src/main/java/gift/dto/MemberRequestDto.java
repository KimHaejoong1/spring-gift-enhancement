package gift.dto;

import gift.domain.Member;
import jakarta.validation.constraints.Email;

public record MemberRequestDto(
        @Email
        String email,
        String password
) {
        public Member toDomain() {
                return new Member(email, password);
        }
}
