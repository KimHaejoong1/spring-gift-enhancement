package gift.validation.itemPolicy.ItemViolationHandler;

import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;


@Component
public class ItemNameViolationHandler implements ViolationHandler {
    @Override
    public void addViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addPropertyNode("name").addConstraintViolation();
    }
}
