package gift.exception;

public class WishNotFoundByMemberIdAndWishId extends RuntimeException {
    public WishNotFoundByMemberIdAndWishId(Long memberId, Long wishId) {
        super(memberId + ":" + wishId + "에 해당하는 위시리스트가 존재하지 않습니다.");
    }
}
