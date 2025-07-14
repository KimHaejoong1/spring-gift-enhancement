package gift.dto;

import gift.config.NotHaveValue;
import gift.config.SpecialChar;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateProductRequest (
        Long id,
        @NotBlank(message = "상품명은 필수입니다.")
        @Size(max = 15, message = "15자를 넘을 수 없습니다.")
        @SpecialChar(message = "(), [], +, -, &, /, _ 이외의 특수문자는 사용할 수 없습니다")
        @NotHaveValue(message = "카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.",value = "카카오")
        String name,
        @Positive(message = "가격은 0보다 커야합니다.")
        @NotNull(message = "가격은 필수입니다.")
        Integer price,
        @NotBlank(message = "URL을 입력해야 합니다.")
        String imageUrl){
}
