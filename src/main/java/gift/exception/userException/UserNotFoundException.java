package gift.exception.userException;

import gift.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException(String email) {
        super(HttpStatus.NOT_FOUND, "해당 이메일 사용자를 찾을 수 없습니다.: " + email);
    }

    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다.");
    }
}
