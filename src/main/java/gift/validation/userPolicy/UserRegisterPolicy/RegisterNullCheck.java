package gift.validation.userPolicy.UserRegisterPolicy;

import gift.dto.userDto.UserRegisterDto;
import gift.validation.userPolicy.UserPolicy;
import gift.validation.userPolicy.UserViolationHandler.ViolationHandler;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class RegisterNullCheck implements UserPolicy<UserRegisterDto> {

    private final ViolationHandler violationHandler;

    public RegisterNullCheck(@Qualifier("userPasswordValidationHandler") ViolationHandler violationHandler) {
        this.violationHandler = violationHandler;
    }

    @Override
    public boolean isValid(UserRegisterDto dto, ConstraintValidatorContext context) {
        if (dto.email() == null) {
            violationHandler.addViolation(context, "필수 입력 값 입니다.");
            return false;
        }
        return true;
    }
}
