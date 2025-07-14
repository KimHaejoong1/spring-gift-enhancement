package gift.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductRequestDto(
    @NotBlank(message = "상품 이름은 필수입니다.")
    @Size(max=15, message = "상품 이름은 공백 포함 최대 15자까지 입력할 수 있습니다.")
    @Pattern(
        regexp = "^$|^[a-zA-Z0-9가-힣\\s()\\[\\]+\\-&/_]+$",
        message = "허용되지 않는 특수문자가 포함되어 있습니다."
    )
    @Pattern(
        regexp = "^((?!카카오).)*$",
        message = "'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다."
    )
    String name,

    @PositiveOrZero(message = "0 이상 값을 가져야 합니다.")
    int price,

    @NotBlank(message = "이미지 URL은 필수입니다.")
    String imageUrl
) {}