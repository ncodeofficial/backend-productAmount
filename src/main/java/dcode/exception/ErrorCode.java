package dcode.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 BAD_REQUEST : 잘못된 요청
    INVALID_PRODUCT(BAD_REQUEST, "잘못된 상품 정보 입니다");

    private final HttpStatus httpStatus;
    private final String message;

}
