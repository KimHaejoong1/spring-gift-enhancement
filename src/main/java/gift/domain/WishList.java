package gift.domain;

public class WishList {
    private Long id;
    private Long memberId;
    private Long productId;
    private int quantity;

    public WishList(Long id, Long memberId, Long productId, int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
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


    public void update(int quantity){
        this.quantity += quantity;

        if(this.quantity<0)     this.quantity=0;
    }
}
