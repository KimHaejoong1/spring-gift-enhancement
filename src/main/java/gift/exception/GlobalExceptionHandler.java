package gift.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.SQLException;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleArgumentNotValidException(
        MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        List<ErrorResponse.ValidationError> validationErrorList = bindingResult.getFieldErrors()
            .stream()
            .map(fieldError -> ErrorResponse.ValidationError.of(
                fieldError.getField(),
                fieldError.getDefaultMessage()
            ))
            .toList();

        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, validationErrorList);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedShaAlgorithmException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedShaAlgorithmException(
        UnsupportedShaAlgorithmException exception
    ) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.NO_SUPPORTED_SHA256_ALGORITHM);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnAuthenicatedException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthenicatedException(
        UnAuthenicatedException exception
    ) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.UNAUTHENICATED_LOGIN);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
        DataIntegrityViolationException exception
    ) {
        Throwable rootCause = exception.getMostSpecificCause();

        if (rootCause instanceof SQLException sqlException) {
            String sqlState = sqlException.getSQLState();

            if ("23505".equals(sqlState)) {
                ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.DUPLICATE_RESOURCE);
                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
            } else if ("23506".equals(sqlState)) {
                ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.DATA_INTEGRITY_ERROR);
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        }

        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.DATABASE_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(
        ExpiredJwtException exception
    ) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.EXPIRED_TOKEN_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleSignatureException(
        JwtException exception
    ) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_TOKEN_SIGNATURE);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
        ResourceNotFoundException exception
    ) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.RESOURCE_NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}