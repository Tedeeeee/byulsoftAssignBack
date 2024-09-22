package project.assign.commonApi;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import project.assign.exception.ErrorCode;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    private HttpStatus status;
    private int statusCode;
    private String message;

    @Builder
    public ErrorResponse(final ErrorCode errorCode, String message) {
        this.status = errorCode.getStatus();
        this.statusCode = errorCode.getStateCode();
        this.message = message;
    }
}
