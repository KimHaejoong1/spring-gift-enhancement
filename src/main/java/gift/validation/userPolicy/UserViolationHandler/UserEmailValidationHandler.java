package gift.validation.userPolicy.UserViolationHandler;

import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UserEmailValidationHandler implements ViolationHandler{
    @Override
    public void addViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addPropertyNode("email").addConstraintViolation();
    }
}
