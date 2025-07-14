package gift.exception;

// 인가
public class AuthorizationException extends RuntimeException {

    public AuthorizationException(String message) {
        super(message);
    }
}
