package gift.validation.itemPolicy.ItemViolationHandler;

import jakarta.validation.ConstraintValidatorContext;


public interface ViolationHandler {
    void addViolation(ConstraintValidatorContext context, String message);
}