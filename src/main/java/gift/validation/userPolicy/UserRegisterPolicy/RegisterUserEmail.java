package gift.validation.userPolicy.UserRegisterPolicy;

import gift.dto.userDto.UserRegisterDto;
import gift.validation.userPolicy.UserPolicy;
import gift.validation.userPolicy.UserViolationHandler.ViolationHandler;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class RegisterUserEmail implements UserPolicy<UserRegisterDto> {

    private final ViolationHandler violationHandler;

    public RegisterUserEmail(@Qualifier("userEmailValidationHandler") ViolationHandler violationHandler) {
        this.violationHandler = violationHandler;
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Override
    public boolean isValid(UserRegisterDto dto, ConstraintValidatorContext context) {
        if (dto.email()==null || !EMAIL_PATTERN.matcher(dto.email()).matches()) {
            violationHandler.addViolation(context,"이메일 형식과 맞지 않습니다.");
            return false;
        }
        return true;
    }
}
