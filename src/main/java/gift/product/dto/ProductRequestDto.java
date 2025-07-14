package gift.product.dto;

import gift.product.entity.Product;
import jakarta.validation.constraints.*;

public class ProductRequestDto {

    @Size(min = 1 , max = 15,message = "이름은 1~15자 여야합니다")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_ ]*$", message = "특수문자는 (), [], +, -, &, /, _ 만 허용됩니다.")
    private String name;

    @NotNull(message = "비울 수 없습니다")
    private Integer price;
    private String imageUrl;

    public ProductRequestDto() {}
    public ProductRequestDto(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
    }

    public static ProductRequestDto fromEntity(Product product) {
        return new ProductRequestDto(product);
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
}
