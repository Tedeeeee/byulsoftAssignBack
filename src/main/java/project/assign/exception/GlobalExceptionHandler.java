package project.assign.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.assign.commonApi.CommonResponse;
import project.assign.commonApi.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessExceptionHandler.class)
    public CommonResponse<ErrorResponse> handleCustomException(BusinessExceptionHandler ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getHttpStatus(), ex.getStatusCode(), ex.getMessage());
        return CommonResponse.fail(errorResponse.getStatusCode(), errorResponse, errorResponse.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public CommonResponse<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 오류 발생");
        return CommonResponse.fail(errorResponse.getStatusCode(), errorResponse, errorResponse.getMessage());
    }
}
