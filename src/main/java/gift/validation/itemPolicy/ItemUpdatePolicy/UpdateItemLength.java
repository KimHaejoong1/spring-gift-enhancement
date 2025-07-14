package gift.validation.itemPolicy.ItemUpdatePolicy;

import gift.dto.itemDto.ItemUpdateDto;
import gift.validation.itemPolicy.ItemPolicy;
import gift.validation.itemPolicy.ItemViolationHandler.ViolationHandler;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UpdateItemLength implements ItemPolicy<ItemUpdateDto> {
    private final ViolationHandler violationHandler;

    public UpdateItemLength(ViolationHandler violationHandler) {
        this.violationHandler = violationHandler;
    }

    @Override
    public boolean isValid(ItemUpdateDto dto, ConstraintValidatorContext context) {
        if (dto.name().length() > 15) {
            System.out.println("글자길이 오류");
            violationHandler.addViolation(context, "상품 이름은 최대 15자까지 입니다.");
            return false;
        }
        return true;
    }
}
