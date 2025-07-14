package gift.validation.itemPolicy;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ItemCreateValidator.class, ItemUpdateValidator.class})

public @interface ItemFieldValid {
    String message() default "이름이 유효하지 않습니다.";

    Class<?>[] groups() default {}; //유효성 검사 그룹 지정

    Class<? extends Payload>[] payload() default {}; // 메타데이터 전달용

}
