package dcode.service;

import dcode.config.PromotionProperties;
import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.domain.entity.DiscountType;
import dcode.exception.InvalidRequestPropertyException;
import dcode.exception.NoSuchProductException;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import dcode.repository.PromotionRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentAccessException;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @InjectMocks
  ProductService productService;

  @Mock
  ProductRepository productRepository;

  @Mock
  PromotionRepository promotionRepository;

  @BeforeEach
  void testConfigs() {
    assertEquals(1_000, PromotionProperties.MIN_PRICE_UNIT);
    assertEquals(10_000, PromotionProperties.MIN_FINAL_DISCOUNTED_PRICE);
  }

  @Test
  @DisplayName("bean validation 적용여부 체크")
  void getProduct(){
    var invalidProduct = ProductInfoRequest.builder().productId(-1).promotionIds(null).build();

    assertThrows(InvalidRequestPropertyException.class, () -> productService.getProductAmount(invalidProduct));
  }

  @Test
  @DisplayName("프로모션 적용된 상품가격 조회 : 존재하지 않는(DB에 없는) 프로덕트일 경우 NoSuchProductException 발생")
  void getProductAmount_1() {
    // given
    var requestBody = ProductInfoRequest.builder().productId(1).promotionIds(new int[]{1, 2}).build();
    // when
    when(productRepository.getProduct(requestBody.getProductId())).thenReturn(Optional.empty());

    // then
    assertThrows(NoSuchProductException.class, () -> productService.getProductAmount(requestBody));

    verify(productRepository, times(1)).getProduct(requestBody.getProductId());
    verifyNoInteractions(promotionRepository);
  }

  @ParameterizedTest(name = "{index}: {0}")
  @MethodSource("getProductAmountParameters")
  @DisplayName("계산 제대로 되는지 테스트")
  void getProductAmount(String description, ProductInfoRequest productInfoRequest, Product product, List<Promotion> promotionList, ProductAmountResponse expected) {
    // when
    when(productRepository.getProduct(anyInt())).thenReturn(Optional.ofNullable(product));
    when(promotionRepository.getPromotionsByProductIdAndPromotionId(anyInt(), any(int[].class))).thenReturn(promotionList);

    // then
    var actual = productService.getProductAmount(productInfoRequest);
    assertEquals(expected, actual);
  }

  private static Stream<Arguments> getProductAmountParameters() {
    return Stream.of(
      case2(),
      case3(),
      case4(),
      case5()
    );
  }

  private static Arguments case2() {
    String description = "최종 할인가격이 10000원 미만일경우, PERECENT 할인율";
    int productId = 1;
    int originalPrice = 19_000;
    int discountInWon = 50;
    String productName = "Blue Jeans";

    var productInfoRequest = ProductInfoRequest.builder().productId(productId).promotionIds(new int[]{}).build();
    var product = Product.builder().id(productInfoRequest.getProductId()).name(productName).price(originalPrice).build();
    var coupon1 = Promotion.builder()
      .id(1)
      .promotion_type("COUPON")
      .name("Int.MAX 원 할인쿠폰")
      .discount_type(DiscountType.PERCENT)
      .discount_value(discountInWon)
      .use_started_at(new Date(System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(1)))
      .use_ended_at(new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1))).build();
    var expectedResponse = ProductAmountResponse.builder()
      .name(productName)
      .originPrice(originalPrice)
      .discountPrice(9_000)
      .finalPrice(10_000)
      .build();

    return Arguments.of(
      description, productInfoRequest, product, List.of(coupon1), expectedResponse
    );
  }

  private static Arguments case3() {
    String description = "최종 할인가격이 10000원 미만일경우, WON 할인금액";
    int productId = 1;
    int originalPrice = 10_003;
    int discountInWon = Integer.MAX_VALUE;
    String productName = "Blue Jeans";

    var productInfoRequest = ProductInfoRequest.builder().productId(productId).promotionIds(new int[]{}).build();
    var product = Product.builder().id(productInfoRequest.getProductId()).name(productName).price(originalPrice).build();
    var coupon1 = Promotion.builder()
      .id(1)
      .promotion_type("COUPON")
      .name("Int.MAX 원 할인쿠폰")
      .discount_type(DiscountType.WON)
      .discount_value(discountInWon)
      .use_started_at(new Date(System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(1)))
      .use_ended_at(new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1))).build();
    var expectedResponse = ProductAmountResponse.builder()
      .name(productName)
      .originPrice(originalPrice)
      .discountPrice(3)
      .finalPrice(10_000)
      .build();

    return Arguments.of(
      description, productInfoRequest, product, List.of(coupon1), expectedResponse
    );
  }


  private static Arguments case4() {
    String description = "할인 적용 기간들이 아닌경우";
    int originalPrice = 50_000;

    var productInfoRequest = ProductInfoRequest.builder().productId(1).promotionIds(new int[]{}).build();
    var product = Product.builder().id(productInfoRequest.getProductId()).name("Blue Jeans").price(originalPrice).build();
    var coupon1 = Promotion.builder()
      .id(1)
      .promotion_type("COUPON")
      .name("30_000원 할인쿠폰")
      .discount_type(DiscountType.WON)
      .discount_value(1)
      .use_started_at(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)))
      .use_ended_at(new Date(System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(1))).build();
    var coupon2 = Promotion.builder()
      .id(1)
      .promotion_type("CODE")
      .name("50퍼센트 할인")
      .discount_type(DiscountType.PERCENT)
      .discount_value(50)
      .use_started_at(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)))
      .use_ended_at(new Date(System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(1))).build();
    var expectedResponse = ProductAmountResponse.builder()
      .name("Blue Jeans")
      .originPrice(originalPrice)
      .discountPrice(0)
      .finalPrice(originalPrice)
      .build();

    return Arguments.of(
      description, productInfoRequest, product, List.of(coupon1, coupon2), expectedResponse
    );
  }

  private static Arguments case5() {
    String description = "1000원 단위로 절삭되는지 확인하기, 50_000원 - 1원 = 49_999원 ~ 49000원";
    int productId = 1;
    int originalPrice = 50_000;
    int discountInWon = 1;
    String productName = "Blue Jeans";

    var productInfoRequest = ProductInfoRequest.builder().productId(productId).promotionIds(new int[]{}).build();
    var product = Product.builder().id(productInfoRequest.getProductId()).name(productName).price(originalPrice).build();
    var coupon = Promotion.builder()
      .id(1)
      .promotion_type("COUPON")
      .name("30_000원 할인쿠폰")
      .discount_type(DiscountType.WON)
      .discount_value(discountInWon)
      .use_started_at(new Date(System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(1)))
      .use_ended_at(new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1))).build();
    var expectedResponse = ProductAmountResponse.builder()
      .name(productName)
      .originPrice(originalPrice)
      .discountPrice(1000)
      .finalPrice(49000)
      .build();

    return Arguments.of(
      description, productInfoRequest, product, List.of(coupon), expectedResponse
    );
  }
}