package gift.dto;

import gift.common.annotation.ForbiddenKeyword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductRequest(

    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 15, message = "상품명은 15자 까지만 입력 가능합니다.")
    @Pattern(regexp = "^[\\s\\w가-힣-+()&/\\[\\]]+$", message = "지원하지 않는 문자가 포함되어있습니다.")
    @ForbiddenKeyword
    String name,
    @NotNull(message = "가격은 필수입니다.")
    Integer price,
    @NotBlank(message = "이미지 URL은 필수입니다.")
    String imageUrl

) {

    public static ProductRequest from(ProductResponse response) {
        return new ProductRequest(
            response.name(),
            response.price(),
            response.imageUrl()
        );
    }
}
