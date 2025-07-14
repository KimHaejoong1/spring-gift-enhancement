package gift.wishproduct.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class WishProductCreateReq {

    @NotNull
    private UUID productId;
    @Min(value = 1, message = "1개 이상 주문이 가능합니다.")
    private int quantity;

    public WishProductCreateReq(UUID productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    protected WishProductCreateReq() {
    }

    public UUID getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
