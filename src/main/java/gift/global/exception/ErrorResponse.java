package gift.global.exception;


public record ErrorResponse(
    ErrorCode errorCode,
    String message
) {}
