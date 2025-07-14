package gift.dto.userDto;

import jakarta.validation.constraints.NotNull;

public record UserLoginDto(@NotNull String email, @NotNull String password) {
}
