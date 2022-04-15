package dcode.product;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import dcode.domain.entity.DiscountType;
import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.domain.entity.PromotionProducts;
import dcode.domain.entity.PromotionType;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductJpaRepository;
import dcode.repository.PromotionJpaRepository;
import dcode.repository.PromotionProductsJpaRepository;
import dcode.service.ErrorMessages;
import dcode.service.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductJpaRepository productRepository;

	@Mock
	private PromotionJpaRepository promotionRepository;

	@Mock
	private PromotionProductsJpaRepository promotionProductsRepository;

	@InjectMocks
	private ProductService productService;

	@BeforeEach
	void init() {
		final Product product = Product.builder()
			.name("디코드상품")
			.price(215000)
			.build();
		ReflectionTestUtils.setField(product, "id", 1);
		doReturn(Optional.of(product)).when(productRepository).findById(any(Integer.class));

		doReturn(Optional.of(mock(PromotionProducts.class))).when(promotionProductsRepository)
			.findByProductAndPromotion(any(Product.class), any(Promotion.class));
	}

	@Test
	@DisplayName("상품 가격 조회")
	void getProductAmount() throws Exception {
		// given
		final ProductInfoRequest request = ProductInfoRequest.builder()
			.productId(1)
			.couponIds(List.of(1, 2))
			.build();

		mockPromotion(1, PromotionType.COUPON, "30000원 할인쿠폰", 30000, DiscountType.WON, Date.valueOf("2022-02-01"),
			Date.valueOf("3023-05-01"));
		mockPromotion(2, PromotionType.CODE, "15% 할인코드", 15, DiscountType.PERCENT, Date.valueOf("2022-02-01"),
			Date.valueOf("3023-05-01"));

		// when
		final ProductAmountResponse response = productService.getProductAmount(request);

		// then
		assertThat(response.getFinalPrice()).isEqualTo(152000);
		assertThat(response.getDiscountPrice()).isEqualTo(62250);
	}

	@Test
	@DisplayName("상품 가격 조회_최소 금액 적용")
	void getProductAmount_MinimumProductPrice() throws Exception {
		// given
		final ProductInfoRequest request = ProductInfoRequest.builder()
			.productId(1)
			.couponIds(List.of(1, 2))
			.build();

		mockPromotion(1, PromotionType.COUPON, "180000원 할인쿠폰", 180000, DiscountType.WON, Date.valueOf("2022-02-01"),
			Date.valueOf("3023-05-01"));
		mockPromotion(2, PromotionType.CODE, "15% 할인코드", 15, DiscountType.PERCENT, Date.valueOf("2022-02-01"),
			Date.valueOf("3023-05-01"));

		// when
		final ProductAmountResponse response = productService.getProductAmount(request);

		// then
		assertThat(response.getFinalPrice()).isEqualTo(10000);
		assertThat(response.getDiscountPrice()).isEqualTo(212250);
	}

	@Test
	@DisplayName("상품 가격 조회_사용 기간이 아닌 프로모션 사용")
	void getProductAmount_ExpiredPromotion() throws Exception {
		// given
		final ProductInfoRequest request = ProductInfoRequest.builder()
			.productId(1)
			.couponIds(List.of(1, 2))
			.build();

		mockPromotion(1, PromotionType.COUPON, "180000원 할인쿠폰", 180000, DiscountType.WON, Date.valueOf("2022-02-01"),
			Date.valueOf("2022-04-01"));

		// when
		final Executable executable = () -> productService.getProductAmount(request);

		// then
		assertThrows(IllegalArgumentException.class, executable, ErrorMessages.PROMOTION_INVALID_PERIOD.getMessage());
	}

	Promotion mockPromotion(Integer id, PromotionType promotionType, String name, int discountValue,
		DiscountType discountType, Date userStartedAt, Date useEndedAt) {
		final Promotion promotion = Promotion.builder()
			.promotionType(promotionType)
			.name(name)
			.discountType(discountType)
			.discountValue(discountValue)
			.useStartedAt(userStartedAt)
			.useEndedAt(useEndedAt)
			.build();
		ReflectionTestUtils.setField(promotion, "id", id);
		doReturn(Optional.of(promotion)).when(promotionRepository).findById(id);

		return promotion;
	}
}