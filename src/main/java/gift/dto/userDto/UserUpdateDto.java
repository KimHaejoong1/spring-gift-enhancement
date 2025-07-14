package gift.dto.userDto;

import jakarta.validation.constraints.NotNull;

public record UserUpdateDto(@NotNull String email, @NotNull String password) {
}
