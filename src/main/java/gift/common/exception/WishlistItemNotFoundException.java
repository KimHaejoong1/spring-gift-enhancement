package gift.common.exception;

public class WishlistItemNotFoundException extends RuntimeException {
    public WishlistItemNotFoundException() {
        super("위시리스트에 해당 상품이 존재하지 않습니다.");
    }
}
