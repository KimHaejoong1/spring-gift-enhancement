package gift.dto.product;

/*
 ProductRequest와 ProductResponse 현재 동일한 코드 구조를 가지고 있어 하나의 dto로 합쳐도 되지만,
 이후에 기능 확장, 필드 추가 등을 고려해 미리 분리해 놓는 게 낫다고 판단했습니다.
*/

import gift.domain.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record ProductRequest(
    Long id,

    @NotNull(message = "상품명은 필수값입니다.")
    @Size(max = 15, message = "상품명은 최대 15자까지 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_\\s]+$", message="사용가능한 특수문자: (), [], +, -, &, /, _")
    String name,

    @Range(min = 0, message="상품 가격은 0 이상이어야 합니다.")
    @NotNull(message = "상품 가격은 필수값입니다.")
    Integer price,

    @Size(max=500, message="이미지 url의 길이는 500자 이하여야 합니다.")
    String imageUrl
) {

    public static ProductRequest NewForm(){
        return new ProductRequest(null, null, 0, null);
    }

    public static ProductRequest from(Product product){
        return new ProductRequest(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl()
        );
    }

}
