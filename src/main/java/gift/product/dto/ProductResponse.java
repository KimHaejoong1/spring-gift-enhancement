package gift.product.dto;

import gift.domain.Product;

import java.util.UUID;

public class ProductResponse {

    private UUID id;
    private String name;
    private int price;
    private String imageURL;
    private UUID memberId;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageURL = product.getImageURL();
        this.memberId = product.getMemberId();
    }

    protected ProductResponse() {}
  
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public UUID getMemberId() {
        return memberId;
    }
}
