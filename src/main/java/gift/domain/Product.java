package gift.domain;

public class Product {

    public enum Status {
        APPROVED,
        PENDING,
        REJECTED
    }

    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;
    private Status status;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isApproved() {
        return status == Status.APPROVED;
    }

    public Product(String name, int price, String imageUrl) {
        this(-1L, name, price, imageUrl);
    }

    public Product(Long id, String name, int price, String imageUrl, Status status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public Product(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        status = inferStatus(name);
    }

    private Status inferStatus(String name) {
        if (name.contains("카카오")) {
            return Product.Status.PENDING;
        } else {
            return Product.Status.APPROVED;
        }
    }
}
