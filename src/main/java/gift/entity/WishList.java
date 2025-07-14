package gift.entity;

public class WishList {
    private Integer id;
    private Integer memberId;
    private Integer productId;
    private Integer quantity;

    public WishList() {}

    public WishList(Integer id, Integer memberId, Integer productId, Integer quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }
    public Integer getMemberId() {
        return memberId;
    }
    public Integer getProductId() {
        return productId;
    }
    public Integer getQuantity() {
        return quantity;
    }
}
