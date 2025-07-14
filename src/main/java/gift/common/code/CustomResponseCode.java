package gift.common.code;

import org.springframework.http.HttpStatus;

public enum CustomResponseCode {

    CREATED(201, "생성 성공", HttpStatus.CREATED),
    RETRIEVED(200, "조회 성공", HttpStatus.OK),
    UPDATED(200, "수정 성공", HttpStatus.OK),
    DELETED(204, "삭제 성공", HttpStatus.NO_CONTENT),
    LIST_RETRIEVED(200, "목록 조회 성공", HttpStatus.OK),

    LOGIN_SUCCESS(200, "로그인 성공!", HttpStatus.OK),
    VALIDATION_FAILED(400, "요청 값이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(401, "로그인이 필요합니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    LOGIN_FAILED(403, "로그인에 실패!", HttpStatus.FORBIDDEN),
    FORBIDDEN_KEYWORD(403, "%s - 해당 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.", HttpStatus.FORBIDDEN),
    NOT_FOUND(404, "리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    EMAIL_DUPLICATE(409, "중복된 이메일이 이미 존재합니다。", HttpStatus.CONFLICT),
    ALREADY_EXISTS(409, "이미 등록된 항목입니다.", HttpStatus.CONFLICT),
    DB_ERROR(500, "데이터베이스 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_ERROR(500, "서버 내부 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    CustomResponseCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
