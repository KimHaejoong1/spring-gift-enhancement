package gift.domain;

import java.util.UUID;

public class WishProduct {

    private UUID id;
    private int quantity;
    private UUID ownerId;
    private UUID productId;


    public WishProduct(UUID id, int quantity, UUID ownerId, UUID productId) {
        this.id = id;
        this.quantity = quantity;
        this.ownerId = ownerId;
        this.productId = productId;
    }

    public WishProduct(int quantity, UUID ownerId, UUID productId) {
        this.id = UUID.randomUUID();
        this.quantity = quantity;
        this.ownerId = ownerId;
        this.productId = productId;
    }

    protected WishProduct() {}

    public UUID getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public UUID getProductId() {
        return productId;
    }

}
