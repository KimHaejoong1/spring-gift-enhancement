package gift.exception.itemException;

import gift.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ItemDuplicatedException extends ApplicationException {
    public ItemDuplicatedException() {
        super(HttpStatus.CONFLICT, "이미 존재하는 아이템 입니다.");
    }
}
