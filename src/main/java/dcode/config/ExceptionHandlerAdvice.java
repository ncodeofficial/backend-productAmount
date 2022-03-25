package dcode.config;

import dcode.exception.InvalidRequestPropertyException;
import dcode.exception.NoSuchProductException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionHandlerAdvice {

  @ExceptionHandler(
    value = {
      NoSuchProductException.class
    })
  public ResponseEntity<?> handleNoSuchProductException(Throwable e) {
    return ResponseEntity.badRequest().body("product information not found by id");

  }

  @ExceptionHandler(
    value = {
      InvalidRequestPropertyException.class
    })
  public ResponseEntity<?> handleInvalidRequestPropertyException(Throwable e) {
    return ResponseEntity.badRequest().body("INVALID parameters provided");
  }

  @ExceptionHandler(
    value =
      DataAccessException.class
  )
  public ResponseEntity<?> handleDataAccessException(Throwable e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body("Error during Database Access");
  }
}
