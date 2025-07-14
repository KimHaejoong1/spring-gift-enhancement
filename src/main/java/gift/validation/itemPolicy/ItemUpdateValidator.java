package gift.validation.itemPolicy;

import gift.dto.itemDto.ItemUpdateDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ItemUpdateValidator implements ConstraintValidator<ItemFieldValid, ItemUpdateDto> {

    private final List<ItemPolicy<ItemUpdateDto>> policies;

    public ItemUpdateValidator(List<ItemPolicy<ItemUpdateDto>> policies) {
        this.policies = policies;
    }

    @Override
    public boolean isValid(ItemUpdateDto dto, ConstraintValidatorContext context) {
        if (dto == null) return false;
        for (ItemPolicy<ItemUpdateDto> policy : policies) {
            if (!policy.isValid(dto, context)) {
                return false;
            }
        }
        return true;
    }
}
