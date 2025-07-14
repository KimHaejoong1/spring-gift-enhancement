package gift.dto.userDto;

import jakarta.validation.constraints.NotNull;

public record UserResponseDto(@NotNull String email, @NotNull String password) {
}
