package gift.api.wishlist.domain;

import java.time.LocalDateTime;

public class Wishlist {

    private Long id;
    private Long memberId;
    private Long productId;
    private final LocalDateTime createdDate;

    public Wishlist(Long id, Long memberId, Long productId, LocalDateTime createdDate) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.createdDate = createdDate;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
