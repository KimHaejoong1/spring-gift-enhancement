package gift.wishproduct.dto;

import java.util.UUID;

public class WishProductResponse {

    private UUID id;
    private String productName;
    private int price;
    private int quantity;
    private String imageUrl;

    public WishProductResponse(UUID id, String productName, int price, int quantity, String imageUrl) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    protected WishProductResponse() {}

    public UUID getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
