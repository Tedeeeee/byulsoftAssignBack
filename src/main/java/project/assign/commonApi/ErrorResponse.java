package project.assign.commonApi;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private HttpStatus status;
    private int statusCode;
    private String message;

    public ErrorResponse(final HttpStatus httpStatus, final int statusCode, final String message) {
        this.status = httpStatus;
        this.statusCode = statusCode;
        this.message = message;
    }
}
