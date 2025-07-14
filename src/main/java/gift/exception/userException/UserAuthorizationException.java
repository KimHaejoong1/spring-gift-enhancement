package gift.exception.userException;

import gift.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserAuthorizationException extends ApplicationException {
    public UserAuthorizationException() {
        super(HttpStatus.FORBIDDEN, "현재 권한이 없습니다.");
    }

}
