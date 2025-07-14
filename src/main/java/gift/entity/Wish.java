package gift.entity;

public class Wish {
    Long id;
    Long productId;
    Long memberId;

    public Wish(Long productId, Long memberId) {
        this.productId = productId;
        this.memberId = memberId;
    }

    public Wish(Long id, Long productId, Long memberId) {
        this.id = id;
        this.productId = productId;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
