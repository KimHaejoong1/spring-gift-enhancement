package gift.validation.itemPolicy;


import gift.dto.itemDto.ItemCreateDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ItemCreateValidator implements ConstraintValidator<ItemFieldValid, ItemCreateDto> {

    private final List<ItemPolicy<ItemCreateDto>> policies;

    public ItemCreateValidator(List<ItemPolicy<ItemCreateDto>> policies) {
        this.policies = policies;
    }

    @Override
    public boolean isValid(ItemCreateDto dto, ConstraintValidatorContext context) {
        if (dto == null) return false;
        for (ItemPolicy<ItemCreateDto> policy : policies) {
            if (!policy.isValid(dto, context)) {
                return false;
            }
        }
        return true;
    }
}