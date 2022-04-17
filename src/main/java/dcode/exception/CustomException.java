package dcode.exception;


import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@Getter
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = -7806029002430564887L;
    private final ErrorCode errorCode;
    private String exceptionMsg;

    public CustomException(ErrorCode errorCode, String ...exceptionMsg) {
        this.errorCode = errorCode;
        this.exceptionMsg = String.join(", ", exceptionMsg);
    }

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "CustomException {" +
                "status=" + errorCode.getStatus() +
                ", errorCode=" + errorCode.getErrorCode() +
                ", message=" + errorCode.getMessage() +
                ", exceptionMsg=" + exceptionMsg +
                '}';
    }
}
