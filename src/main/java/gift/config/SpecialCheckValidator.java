package gift.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class SpecialCheckValidator implements ConstraintValidator<SpecialChar, String> {


    private static final Pattern FORBIDDEN = Pattern.compile("[^a-zA-Z0-9가-힣()\\[\\]+\\-&/_]");


    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return true;
        return !FORBIDDEN.matcher(value).find();
    }
}
