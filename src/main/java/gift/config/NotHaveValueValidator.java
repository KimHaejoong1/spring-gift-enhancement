package gift.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotHaveValueValidator implements ConstraintValidator<NotHaveValue, String> {

    String required;

    @Override
    public void initialize(NotHaveValue constraintAnnotation) {
        this.required = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return true;
        return !value.contains(required);
    }
}
