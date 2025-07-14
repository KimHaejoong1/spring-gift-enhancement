package gift.validation.userPolicy;

import jakarta.validation.ConstraintValidatorContext;

public interface UserPolicy<T> {
    boolean isValid(T dto, ConstraintValidatorContext context);
}
