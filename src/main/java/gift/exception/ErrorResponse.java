package gift.exception;

import java.util.List;

public class ErrorResponse {
    private String code;
    private String message;
    private List<ValidationError> errors;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorResponse(String code, String message, List<ValidationError> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    public static ErrorResponse of(ErrorCode errorcode) {
        return new ErrorResponse(errorcode.getErrorCode(), errorcode.getMessage());
    }

    public static ErrorResponse of(ErrorCode errorcode, List<ValidationError> errors) {
        return new ErrorResponse(errorcode.getErrorCode(), errorcode.getMessage(), errors);
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public List<ValidationError> getErrors() {
        return this.errors;
    }

    public static class ValidationError {
        private final String field;
        private final String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public static ValidationError of(String field, String message) {
            return new ValidationError(field, message);
        }

        public String getField() {
            return this.field;
        }

        public String getMessage() {
            return this.message;
        }
    }
}