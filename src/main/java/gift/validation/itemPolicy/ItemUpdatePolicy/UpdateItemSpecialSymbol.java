package gift.validation.itemPolicy.ItemUpdatePolicy;

import gift.dto.itemDto.ItemUpdateDto;
import gift.validation.itemPolicy.ItemPolicy;
import gift.validation.itemPolicy.ItemViolationHandler.ViolationHandler;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UpdateItemSpecialSymbol implements ItemPolicy<ItemUpdateDto> {

    private static final Pattern pattern = Pattern.compile("^[a-zA-Z0-9()\\[\\]+\\-\\&/_가-힣ㄱ-ㅎㅏ-ㅣ\\s]*$");

    private final ViolationHandler violationHandler;

    public UpdateItemSpecialSymbol(ViolationHandler violationHandler) {
        this.violationHandler = violationHandler;
    }

    @Override
    public boolean isValid(ItemUpdateDto dto, ConstraintValidatorContext context) {
        if (!pattern.matcher(dto.name()).matches()) {
            System.out.println("특수문자 오류");
            violationHandler.addViolation(context, "( ), [ ], +, -, &, /, _\" 외에는 특수 문자가 허용되지 않습니다.");
            return false;
        }
        return true;
    }
}
