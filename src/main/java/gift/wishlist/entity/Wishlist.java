package gift.wishlist.entity;


public class Wishlist {
    private Long id;
    private Long memberId;
    private Long productId;
    private int quantity;

    public Wishlist(Long id, Long memberId, Long productId, int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {return this.id;}
    public Long getMemberId() {return this.memberId;}
    public Long getProductId() {return this.productId;}
    public int getQuantity() {return this.quantity;}
}
