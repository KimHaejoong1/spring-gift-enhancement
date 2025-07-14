package gift.domain;


import gift.dto.product.ProductRequest;

public class Product {

    private Long id;
    private String name;
    private int price;
    private String imageUrl;

    public Product(){}

    public Product(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static Product of(String name, int price, String imageUrl) {
        return new Product(null, name, price, imageUrl);
    }

    public static Product from(ProductRequest request){
        return new Product(
            request.id(),
            request.name(),
            request.price(),
            request.imageUrl()
        );
    }

    public Product update(String name, Integer price, String imageUrl) {

        if(name!=null)
            this.name = name;

        if(price!=null)
            this.price = price;

        if(imageUrl!=null)
            this.imageUrl = imageUrl;

        return this;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }
}
