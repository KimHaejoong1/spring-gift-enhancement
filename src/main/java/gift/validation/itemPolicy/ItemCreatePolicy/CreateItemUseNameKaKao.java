package gift.validation.itemPolicy.ItemCreatePolicy;

import gift.dto.itemDto.ItemCreateDto;
import gift.validation.itemPolicy.ItemPolicy;
import gift.validation.itemPolicy.ItemViolationHandler.ViolationHandler;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;


@Component
public class CreateItemUseNameKaKao implements ItemPolicy<ItemCreateDto> {
    private final ViolationHandler violationHandler;

    public CreateItemUseNameKaKao(ViolationHandler violationHandler) {
        this.violationHandler = violationHandler;
    }

    @Override
    public boolean isValid(ItemCreateDto dto, ConstraintValidatorContext context) {
        if (!dto.useKakaoName() && dto.name().contains("카카오")) {
            violationHandler.addViolation(context, "\"카카오\"는 MD와 협의한 경우에만 사용할 수 있습니다.");

            return false;
        }
        return true;
    }
}
