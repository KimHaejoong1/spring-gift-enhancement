package gift.validation.userPolicy;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UserRegisterValidator.class})
public @interface UserFieldValid {
    String message() default "잘못된 입력입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
