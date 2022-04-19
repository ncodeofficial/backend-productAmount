package dcode.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(UnavailableCouponException.class)
    public ResponseEntity unavailableCouponException(UnavailableCouponException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MinimumPriceException.class)
    public ResponseEntity minimumPriceException(MinimumPriceException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
