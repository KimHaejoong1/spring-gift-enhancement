package gift.common.exception;

public class ForbiddenWordException extends RuntimeException {
    public ForbiddenWordException(String word)  {
        super("상품명에 '" + word + "'를 포함할 수 없습니다.");
    }
}
