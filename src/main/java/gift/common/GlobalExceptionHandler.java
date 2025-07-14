package gift.common;

import gift.member.exception.AuthenticationException;
import gift.member.exception.DuplicatedException;
import gift.product.exception.ProductNotFoundException;
import gift.product.exception.ProductValidationException;
import gift.wishlist.exception.DuplicatedWishException;
import gift.wishlist.exception.WishNotAuthorityException;
import gift.wishlist.exception.WishNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "상품을 찾을 수 없음.",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ProductValidationException.class)
    public ResponseEntity<ErrorResponse> handleProductValidation(ProductValidationException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "유효하지 않은 상품 데이터",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException e) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("message", "입력값이 올바르지 않습니다.");

        Map<String, String> fieldErrors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        });

        errors.put("errors", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "서버 오류",
                "서버 오류가 발생했습니다."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "권한 오류",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicatedException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "중복 이메일",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(WishNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWishNotFound(WishNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "위시리스트를 찾을 수 없음",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DuplicatedWishException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedWish(DuplicatedWishException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "중복 위시리스트",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(WishNotAuthorityException.class)
    public ResponseEntity<ErrorResponse> handleWishNotAuthority(WishNotAuthorityException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "권한없는 접근",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    public record ErrorResponse(String errorCode, String message) {}
}
