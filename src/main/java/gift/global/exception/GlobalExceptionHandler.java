package gift.global.exception;

import io.jsonwebtoken.JwtException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleInvalidProductException(CustomException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
            .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    // jdbcTemplate.query()의 결과값이 없을 때
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleDataAccessException(EmptyResultDataAccessException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(ErrorCode.NOT_EXISTS, ErrorCode.NOT_EXISTS.getErrorMessage()));
    }

    // validation 관련 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        List<ErrorResponse> errors = new ArrayList<>();

        e.getBindingResult().getAllErrors().forEach(error -> {
            errors.add(
                new ErrorResponse(ErrorCode.INVALID_FORM_REQUEST, error.getDefaultMessage()));
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errors);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleJwtException(JwtException e) {
        return ResponseEntity.status(ErrorCode.INVALID_TOKEN_REQUEST.getHttpStatus())
            .body(new ErrorResponse(ErrorCode.INVALID_TOKEN_REQUEST,
                ErrorCode.INVALID_TOKEN_REQUEST.getErrorMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(ErrorCode.OTHERS, e.getMessage()));
    }

}
