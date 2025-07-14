package gift.dto;

import gift.domain.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record ProductRequestDto(
        @Length(max = 15)
        @Pattern(regexp= "^[a-zA-Z0-9가-힣()\\[\\]+&/ _-]+$",
                message = "The following special characters are allowed: (), [], +, &, /, _, -]")
        String name,
        @Min(0) Integer price,
        @Length(max = 255) String imageUrl) {

    public Product toEntity() {
        return new Product(name, price, imageUrl);
    }
}

