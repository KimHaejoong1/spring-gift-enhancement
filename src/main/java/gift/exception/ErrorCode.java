package gift.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_INPUT_VALUE(
        HttpStatus.BAD_REQUEST,
        "P001",
        "상품 입력값이 유효하지 않습니다."
    ),

    NO_SUPPORTED_SHA256_ALGORITHM(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "E001",

        "SHA-256 알고리즘을 지원하지 않습니다."
    ),

    UNAUTHENICATED_LOGIN(
        HttpStatus.UNAUTHORIZED,
        "E002",
        "로그인이 필요합니다. 인증되지 않은 사용자입니다."
    ),

    EXPIRED_TOKEN_ERROR(
        HttpStatus.BAD_REQUEST,
        "T001",
        "인증 토큰이 만료되었습니다. 다시 로그인해주세요."
    ),

    INVALID_TOKEN_SIGNATURE(
        HttpStatus.UNAUTHORIZED,
        "T002",
        "유효하지 않은 토큰입니다. 토큰이 변조되었거나 위조되었습니다."
    ),

    DUPLICATE_RESOURCE(
        HttpStatus.CONFLICT,
        "D001",
        "이미 등록된 리소스입니다."
    ),

    DATA_INTEGRITY_ERROR(
        HttpStatus.BAD_REQUEST,
        "D002",
        "연관된 리소스를 찾을 수 없습니다. 존재하지 않는 회원 또는 상품입니다."
    ),

    DATABASE_ERROR(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "D003",
        "데이터베이스 처리 중 오류가 발생했습니다."
    ),

    RESOURCE_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "C001",
        "요청한 리소스를 찾을 수 없습니다."
    );

    private HttpStatus status;
    private String errorCode;
    private String message;

    ErrorCode(HttpStatus status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getMessage() {
        return this.message;
    }
}
