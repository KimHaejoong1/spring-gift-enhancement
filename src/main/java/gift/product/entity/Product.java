package gift.product.entity;

public class Product {
    private Long id;
    private String name;
    private Long price;
    private String imageUrl;
    private Boolean isKakaoApprovedByMd;

    public Product(Long id, String name, Long price, String imageUrl, Boolean isKakaoApprovedByMd) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isKakaoApprovedByMd = isKakaoApprovedByMd;
    }

    public Product(String name, Long price, String imageUrl, Boolean isKakaoApprovedByMd){
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isKakaoApprovedByMd = isKakaoApprovedByMd;
    }

    public Long getId(){return id;}
    public String getName(){return name;}
    public Long getPrice(){return price;}
    public String getImageUrl(){return imageUrl;}
    public Boolean getIsKakaoApprovedByMd(){return isKakaoApprovedByMd;}
}
