package project.assign.commonApi;

import lombok.Builder;
import lombok.Getter;

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
        this.statusCode = statusCode;
        this.body = body;
        this.message = null;
    }
}
