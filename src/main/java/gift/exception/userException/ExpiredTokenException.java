package gift.exception.userException;

import gift.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends ApplicationException {
    public ExpiredTokenException() {
        super(HttpStatus.NOT_FOUND, "토큰이 만료되었습니다.");
    }
}
