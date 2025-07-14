package gift.product.dto;

import gift.product.model.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record ProductRequestDto(

        @NotBlank(message = "상품명은 필수입니다.")
        @Size(max = 15, message = "상품명은 최대 15자까지 입력할 수 있습니다.")
        String name,

        @Min(value = 1, message = "상품가격은 1원 이상이어야 합니다.")
        int price,

        @Pattern(
            regexp = "^((http(s?))\\:\\/\\/)([0-9a-zA-Z\\-]+\\.)+[a-zA-Z]{2,6}(\\:[0-9]+)?(\\/\\S*)?$",
            message = "올바른 URL 형식이 아닙니다."
        )
        String imageUrl
) {

    // 추가 검증 로직 (특수문자, 카카오 키워드)
    public void validateProductName() {
        if (name == null) return;

        // 허용된 특수문자 체크: ( ) [ ] + - & / _
        if (!name.matches("^[a-zA-Z0-9가-힣\\s()\\[\\]+\\-&/_]*$")) {
            throw new IllegalArgumentException("상품명에 허용되지 않은 특수문자가 포함되어 있습니다. 사용 가능한 특수문자: ( ) [ ] + - & / _");
        }

        // 카카오 키워드 체크
        if (name.contains("카카오")) {
            throw new IllegalArgumentException("카카오가 포함된 상품명은 담당 MD와 협의 후 사용 가능합니다.");
        }
    }

    public Product toEntity() {
        return new Product(null, this.name, this.price, this.imageUrl);
    }
}
