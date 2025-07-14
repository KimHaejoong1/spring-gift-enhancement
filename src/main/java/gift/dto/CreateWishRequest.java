package gift.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateWishRequest(@NotNull Long productId, @Positive(message = "수량은 0보다 커야합니다.") int quantity) {
}
