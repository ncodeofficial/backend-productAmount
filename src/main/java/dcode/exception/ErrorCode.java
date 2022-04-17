package dcode.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    RESOURCE_NOT_FOUND(404,"COMMON-ERR-404","RESOURCE NOT FOUND"),
    INTERNAL_SERVER_ERROR(500,"COMMON-ERR-500","INTERNAL SERVER ERROR"),
    ;

    private final int status;
    private final String errorCode;
    private final String message;
}
