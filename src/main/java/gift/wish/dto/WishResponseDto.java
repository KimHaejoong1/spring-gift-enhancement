package gift.wish.dto;

import gift.wish.entity.Wish;

public class WishResponseDto {
    private Long id;
    private Long memberId;
    private Long productId;
    private Integer quantity;

    private WishResponseDto() {};
    private WishResponseDto(Long id, Long memberId, Long productId, Integer quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static WishResponseDto fromEntity(Wish wish) {
        return new WishResponseDto(
                wish.getId(),
                wish.getmemberId(),
                wish.getProductId(),
                wish.getQuantity()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
