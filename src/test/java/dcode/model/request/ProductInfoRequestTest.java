package dcode.model.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.Validation;
import javax.validation.Validator;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProductInfoRequestTest {

  public static final Validator validator =
    Validation.buildDefaultValidatorFactory().getValidator();

  @ParameterizedTest
  @MethodSource("provideRequestsAndViolationSize")
  @DisplayName("Validation 테스트")
  void ValidationTest(ProductInfoRequest request, int violationSize) {
    var violations = validator.validate(request);
    assertEquals(violationSize, violations.size());
  }

  private static Stream<Arguments> provideRequestsAndViolationSize() {
    return Stream.of(
      Arguments.of(new ProductInfoRequest(0, null), 2)
      , Arguments.of(new ProductInfoRequest(-1, null), 2)
      , Arguments.of(new ProductInfoRequest(1, null), 1)
      , Arguments.of(new ProductInfoRequest(1, new int[]{}), 0)
      , Arguments.of(new ProductInfoRequest(1, new int[]{0}), 0)
    );
  }
}