package gift.product.validator;

import gift.product.dto.request.ProductRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class KakaoInNameValidator implements ConstraintValidator<KakaoInName, ProductRequestDto> {
    @Override
    public boolean isValid(ProductRequestDto dto, ConstraintValidatorContext context) {

        if(dto.name() == null || dto.name().isBlank()){
            return true;
        }

        if(dto.name().contains("카카오")){
            if(dto.isKakaoApprovedByMd() == null || !dto.isKakaoApprovedByMd()){

                context.disableDefaultConstraintViolation();

                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode("name")
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
