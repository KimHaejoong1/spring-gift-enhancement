package gift.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateMemberRequest(
        Long id,
        @NotBlank(message = "공백은 불가능합니다.")
        @Email(message = "email 형식을 지켜야합니다.")
        String email,
        @NotBlank(message = "공백은 불가능합니다.")
        String password) {
}
