package gift.exception;

public class UnAuthenicatedException extends RuntimeException {
    public UnAuthenicatedException() {

    }

    public UnAuthenicatedException(String message) {
        super(message);
    }
}
