package gift.exception.itemException;

import gift.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserInputException extends ApplicationException {
    public UserInputException() {
        super(HttpStatus.BAD_REQUEST, "필수 입력값을 확인하세요");
    }
}
