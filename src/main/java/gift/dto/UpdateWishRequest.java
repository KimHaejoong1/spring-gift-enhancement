package gift.dto;

import jakarta.validation.constraints.Positive;

public record UpdateWishRequest (@Positive(message = "수량은 0보다 커야 합니다.") int quantity){
}
