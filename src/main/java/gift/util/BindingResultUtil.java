package gift.util;

import org.springframework.validation.BindingResult;

public abstract class BindingResultUtil {
    public static String getErrorMessage(BindingResult bindingResult) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        bindingResult.getFieldErrors().forEach( fieldError -> {
            errorMessageBuilder.append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append(", ");
        });
        return errorMessageBuilder.toString();
    }
}
