package gift.common.validator;

import gift.common.annotation.ForbiddenKeyword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class ForbiddenKeywordValidator implements ConstraintValidator<ForbiddenKeyword, String> {

    private static final List<String> FORBIDDEN_KEYWORDS = List.of("카카오");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        for (String keyword : FORBIDDEN_KEYWORDS) {
            if (value.contains(keyword)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                    String.format("%s - 해당 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.", keyword)
                ).addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
