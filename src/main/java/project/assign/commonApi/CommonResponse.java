package project.assign.commonApi;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonResponse<T> {
    private final int statusCode;
    private final T body;
    private final String message;

    @Builder
    public CommonResponse(final int statusCode, final T body, final String message) {
        this.statusCode = statusCode;
        this.body = body;
        this.message = message;
    }

    @Builder
    public CommonResponse(final int statusCode, final T body) {
        this(statusCode, body, null);
    }

    public static <T> CommonResponse<T> success(final T body, final String message) {
        return new CommonResponse<>(HttpStatus.OK.value(), body, message);
    }

    public static <T> CommonResponse<T> createSuccess(final String message) {
        return new CommonResponse<>(HttpStatus.CREATED.value(), null, message);
    }

    public static <T> CommonResponse<T> fail(final int statusCode, final T body, final String errorMessage) {
        return new CommonResponse<>(statusCode, body, errorMessage);
    }
}
