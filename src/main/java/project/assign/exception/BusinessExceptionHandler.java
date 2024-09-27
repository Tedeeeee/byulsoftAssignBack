package project.assign.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

// 네이밍 잘못되었다
@Getter
public class BusinessExceptionHandler extends RuntimeException{
    private final HttpStatus httpStatus;
    private final int statusCode;

    public BusinessExceptionHandler(HttpStatus httpStatus, int statusCode, String message) {
       super(message);
        this.httpStatus = httpStatus;
        this.statusCode = statusCode;
    }
}
