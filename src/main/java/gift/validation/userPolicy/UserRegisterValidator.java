package gift.validation.userPolicy;

import gift.dto.userDto.UserRegisterDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class UserRegisterValidator implements ConstraintValidator<UserFieldValid, UserRegisterDto> {

    private final List<UserPolicy<UserRegisterDto>> policies;

    public UserRegisterValidator(List<UserPolicy<UserRegisterDto>> policies) {
        this.policies = policies;
    }

    @Override
    public boolean isValid(UserRegisterDto dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return false;
        }
        for (UserPolicy<UserRegisterDto> policy : policies) {
            if (!policy.isValid(dto, context)) {
                return false;
            }
        }
        return true;
    }
}
