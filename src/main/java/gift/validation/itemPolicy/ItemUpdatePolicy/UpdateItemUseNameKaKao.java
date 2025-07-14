package gift.validation.itemPolicy.ItemUpdatePolicy;

import gift.dto.itemDto.ItemUpdateDto;
import gift.validation.itemPolicy.ItemPolicy;
import gift.validation.itemPolicy.ItemViolationHandler.ViolationHandler;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UpdateItemUseNameKaKao implements ItemPolicy<ItemUpdateDto> {
    private final ViolationHandler violationHandler;

    public UpdateItemUseNameKaKao(ViolationHandler violationHandler) {
        this.violationHandler = violationHandler;
    }

    @Override
    public boolean isValid(ItemUpdateDto dto, ConstraintValidatorContext context) {
        if (!dto.useKakaoName() && dto.name().contains("카카오")) {
            System.out.println("카카오 오류");
            violationHandler.addViolation(context, "\"카카오\"는 MD와 협의한 경우에만 사용할 수 있습니다.");

            return false;
        }
        return true;
    }
}
