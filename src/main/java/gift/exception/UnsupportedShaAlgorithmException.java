package gift.exception;

public class UnsupportedShaAlgorithmException extends RuntimeException {
    public UnsupportedShaAlgorithmException() {

    }

    public UnsupportedShaAlgorithmException(String message, Throwable cause) {
        super(message, cause);
    }
}
