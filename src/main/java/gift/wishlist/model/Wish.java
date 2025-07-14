package gift.wishlist.model;

public class Wish {
    private Long id;
    private Long memberId;
    private Long productId;
    private Long quantity;

    public Wish(Long id, Long memberId, Long productId, Long quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Wish(Long memberId, Long productId, Long quantity) {
        this(null, memberId, productId, quantity);
    }

    public Wish(Long memberId, Long productId) {
        this(null, memberId, productId, 1l); // 기본 수량 1
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

    public Long getQuantity() {
        return quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
