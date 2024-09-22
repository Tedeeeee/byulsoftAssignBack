package project.assign.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 인증, 인가 예외
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,401),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,401),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, 401),
    FORBIDDEN_REQUEST(HttpStatus.FORBIDDEN, 403),

    // 서비스 내부 예외
    CHECK_FAIL(HttpStatus.CONFLICT,409),
    MATCH_FAIL(HttpStatus.FORBIDDEN,401),
    NOT_FOUND(HttpStatus.NOT_FOUND, 404),
    DELETE_FAIL(HttpStatus.CONFLICT,409);

    private final HttpStatus status;
    private final int stateCode;
}
