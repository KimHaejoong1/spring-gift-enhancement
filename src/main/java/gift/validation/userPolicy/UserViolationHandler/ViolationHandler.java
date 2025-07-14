package gift.validation.userPolicy.UserViolationHandler;


import jakarta.validation.ConstraintValidatorContext;

public interface ViolationHandler {
    void addViolation(ConstraintValidatorContext context, String message);
}
