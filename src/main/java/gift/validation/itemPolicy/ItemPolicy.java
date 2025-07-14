package gift.validation.itemPolicy;

import jakarta.validation.ConstraintValidatorContext;

public interface ItemPolicy<T> {
    boolean isValid(T dto, ConstraintValidatorContext context);
}