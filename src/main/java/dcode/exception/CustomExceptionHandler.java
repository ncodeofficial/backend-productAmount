package dcode.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * 커스텀 런타임 에러 : ErrorCode enum class 이용
     */
    @ExceptionHandler(CustomException.class)
    private ResponseEntity<String> customException(CustomException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getErrorCode().getMessage());
    }

    /**
     * 그 외 에러
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<String> Exception(Exception e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

}
