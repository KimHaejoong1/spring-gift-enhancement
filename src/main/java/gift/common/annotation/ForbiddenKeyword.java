package gift.common.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import gift.common.validator.ForbiddenKeywordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ForbiddenKeywordValidator.class)
public @interface ForbiddenKeyword {

    String message() default "{gift.common.annotation.ForbiddenKeyword.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
