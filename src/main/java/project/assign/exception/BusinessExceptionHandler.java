package project.assign.exception;


import lombok.Getter;

@Getter
public class BusinessExceptionHandler extends RuntimeException{
    private final ErrorCode errorCode;

    public BusinessExceptionHandler(ErrorCode errorCode, String message) {
       super(message);
        this.errorCode = errorCode;
    }
}
