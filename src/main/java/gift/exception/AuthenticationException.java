package gift.exception;

// 인증
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }
}
