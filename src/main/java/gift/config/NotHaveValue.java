package gift.config;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NotHaveValueValidator.class)
public @interface NotHaveValue {

    String message();
    String value();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


}
