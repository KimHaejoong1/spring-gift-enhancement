package gift.wishlist.dto;

import gift.wishlist.model.Wish;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WishRequestDto(
        @NotNull(message = "상품 ID는 필수입니다.")
        Long productId,

        @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
        Long quantity
) {
        public Wish toEntity(Long memberId) {
                return new Wish(memberId, this.productId, this.quantity);
        }
}
