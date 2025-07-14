package gift.domain;

import java.util.UUID;

public class Product {

    private UUID id;
    private String name;
    private int price;
    private String imageURL;
    private UUID memberId;

    protected Product() {}

    public Product(String name, int price, String imageURL, UUID memberId) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.price = price;
        this.imageURL = imageURL;
        this.memberId = memberId;
    }

    public Product(UUID id, String name, int price, String imageURL, UUID memberId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageURL = imageURL;
        this.memberId = memberId;
    }

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
