package gift.wishlist.entity;

import java.time.LocalDateTime;

public class Wishlist {

    private Long id;
    private Long memberId;
    private Long productId;
    private int quantity;
    private java.time.LocalDateTime createdAt;

    public Wishlist(Long id,  Long memberId, Long productId, int quantity, java.time.LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
        this.createdAt = createdAt;
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

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
