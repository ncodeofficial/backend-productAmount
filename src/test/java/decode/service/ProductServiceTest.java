package decode.service;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import dcode.repository.PromotionJpaRepository;
import dcode.service.ProductService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;

import static dcode.domain.entity.DiscountType.PERCENT;
import static dcode.domain.entity.DiscountType.WON;
import static dcode.domain.entity.PromotionType.CODE;
import static dcode.domain.entity.PromotionType.COUPON;
import static dcode.service.ProductService.MIN_PRODUCT_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    PromotionJpaRepository promotionJPARepository;

    @InjectMocks
    ProductService productService;

    static int productId;
    static Integer promotionId1;
    static Integer promotionId2;

    static int[] promotionIds;

    static ProductInfoRequest request;

    static Product product;

    static Promotion.PromotionBuilder promotion1Builder;
    static Promotion.PromotionBuilder promotion2Builder;

    @BeforeAll
    static void beforeAll() {
        productId = 1;
        promotionId1 = 1;
        promotionId2 = 2;

        promotionIds = new int[2];
        promotionIds[0] = 1;
        promotionIds[1] = 2;

        request = ProductInfoRequest.builder().productId(1).promotionIds(promotionIds).build();

        product = Product.builder().id(productId).name("상품명").price(50000).build();
        promotion1Builder = Promotion.builder().id(promotionId1).promotion_type(COUPON).discount_type(WON);
        promotion2Builder = Promotion.builder().id(promotionId2).promotion_type(CODE).discount_type(PERCENT);
    }

    @Test
    @DisplayName("할인 상품 조회 성공 - 프로모션 적용")
    void getProductAmountSuccess() {
        //given
        Promotion promotion1 = promotion1Builder.name("10000원 할인쿠폰").discount_value(10000).use_started_at(LocalDate.now().minusDays(1)).use_ended_at(LocalDate.now().plusDays(1)).build();
        Promotion promotion2 = promotion2Builder.name("10% 할인쿠폰").discount_value(10).use_started_at(LocalDate.now().minusDays(1)).use_ended_at(LocalDate.now().plusDays(1)).build();
        int discountPrice = 15000; // 10000원 할인 + 50000원의 10%할인(5000원)

        when(productRepository.getProduct(productId)).thenReturn(product);
        when(promotionJPARepository.findPromotion(productId, promotionId1)).thenReturn(promotion1);
        when(promotionJPARepository.findPromotion(productId, promotionId2)).thenReturn(promotion2);
        //when
        ProductAmountResponse response = productService.getProductAmount(request);
        //then
        assertThat(response.getOriginPrice()).isEqualTo(product.getPrice());
        assertThat(response.getDiscountPrice()).isEqualTo(discountPrice);
        assertThat(response.getFinalPrice()).isEqualTo(product.getPrice()-discountPrice);
    }

    @Test
    @DisplayName("할인 상품 조회 성공 - 사용 가능한 프로모션 없음")
    void getProductAmountSuccess1() {
        //given
        Promotion promotion1 = promotion1Builder.name("10000원 할인쿠폰").discount_value(10000).use_started_at(LocalDate.now().plusDays(1)).use_ended_at(LocalDate.now().plusDays(2)).build();
        Promotion promotion2 = promotion2Builder.name("10% 할인쿠폰").discount_value(10).use_started_at(LocalDate.now().plusDays(1)).use_ended_at(LocalDate.now().plusDays(2)).build();
        int discountPrice = 0; // 사용 가능한 프로모션 없음

        when(productRepository.getProduct(productId)).thenReturn(product);
        when(promotionJPARepository.findPromotion(productId, promotionId1)).thenReturn(promotion1);
        when(promotionJPARepository.findPromotion(productId, promotionId2)).thenReturn(promotion2);
        //when
        ProductAmountResponse response = productService.getProductAmount(request);
        //then
        assertThat(response.getOriginPrice()).isEqualTo(product.getPrice());
        assertThat(response.getDiscountPrice()).isEqualTo(discountPrice);
        assertThat(response.getFinalPrice()).isEqualTo(product.getPrice()-discountPrice);
    }

    @Test
    @DisplayName("할인 상품 조회 성공 - 사용 가능한 프로모션 없음, 1000단위 절삭 안됨")
    void getProductAmountSuccess2() {
        //given
        int product2Id = 2;
        ProductInfoRequest request2 = ProductInfoRequest.builder().productId(product2Id).promotionIds(promotionIds).build();
        Product product2 = Product.builder().id(product2Id).name("상품명").price(19999).build();
        Promotion promotion1 = promotion1Builder.name("10000원 할인쿠폰").discount_value(10000).use_started_at(LocalDate.now().plusDays(1)).use_ended_at(LocalDate.now().plusDays(2)).build();
        Promotion promotion2 = promotion2Builder.name("10% 할인쿠폰").discount_value(10).use_started_at(LocalDate.now().plusDays(1)).use_ended_at(LocalDate.now().plusDays(2)).build();
        int discountPrice = 0; // 사용 가능한 프로모션 없음

        when(productRepository.getProduct(product2Id)).thenReturn(product2);
        when(promotionJPARepository.findPromotion(product2Id, promotionId1)).thenReturn(promotion1);
        when(promotionJPARepository.findPromotion(product2Id, promotionId2)).thenReturn(promotion2);
        //when
        ProductAmountResponse response = productService.getProductAmount(request2);
        //then
        assertThat(response.getOriginPrice()).isEqualTo(product2.getPrice());
        assertThat(response.getDiscountPrice()).isEqualTo(discountPrice);
        assertThat(response.getFinalPrice()).isEqualTo(product2.getPrice()-discountPrice);
    }

    @Test
    @DisplayName("할인 상품 조회 성공 - 할인된 최소 금액 10000")
    void getProductAmountSuccess3() {
        Promotion promotion1 = promotion1Builder.name("50000원 할인쿠폰").discount_value(50000).use_started_at(LocalDate.now().minusDays(1)).use_ended_at(LocalDate.now().plusDays(2)).build();
        Promotion promotion2 = promotion2Builder.name("10% 할인쿠폰").discount_value(10).use_started_at(LocalDate.now().plusDays(1)).use_ended_at(LocalDate.now().plusDays(2)).build();
        int discountPrice = 40000; // 40000원 할인 (5만원 할인 쿠폰이지만 최소 금액이 1만원 이상이여야 하기 떄문에 총 4만원 할인)

        when(productRepository.getProduct(productId)).thenReturn(product);
        when(promotionJPARepository.findPromotion(productId, promotionId1)).thenReturn(promotion1);
        when(promotionJPARepository.findPromotion(productId, promotionId2)).thenReturn(promotion2);
        //when
        ProductAmountResponse response = productService.getProductAmount(request);
        //then
        assertThat(response.getOriginPrice()).isEqualTo(product.getPrice());
        assertThat(response.getDiscountPrice()).isEqualTo(discountPrice);
        assertThat(response.getFinalPrice()).isEqualTo(MIN_PRODUCT_PRICE);
    }

    @Test
    @DisplayName("할인 상품 조회 성공 - 최종 상품 금액 천단위 절삭")
    void getProductAmountSuccess4() {
        Promotion promotion1 = promotion1Builder.name("10000원 할인쿠폰").discount_value(10000).use_started_at(LocalDate.now().minusDays(1)).use_ended_at(LocalDate.now().plusDays(2)).build();
        Promotion promotion2 = promotion2Builder.name("15% 할인쿠폰").discount_value(15).use_started_at(LocalDate.now().minusDays(1)).use_ended_at(LocalDate.now().plusDays(2)).build();
        int discountPrice = 18000; // 10000원 할인 + 50000원의 15%할인(7500원) , 1000단위 절삭

        when(productRepository.getProduct(productId)).thenReturn(product);
        when(promotionJPARepository.findPromotion(productId, promotionId1)).thenReturn(promotion1);
        when(promotionJPARepository.findPromotion(productId, promotionId2)).thenReturn(promotion2);
        //when
        ProductAmountResponse response = productService.getProductAmount(request);
        //then
        assertThat(response.getOriginPrice()).isEqualTo(product.getPrice());
        assertThat(response.getDiscountPrice()).isEqualTo(discountPrice);
        assertThat(response.getFinalPrice()).isEqualTo(product.getPrice()-discountPrice);
    }

    @Test
    @DisplayName("할인 상품 가격 조회 실패 - 존재하지 않는 상품 id ")
    void getProductAmountFail1() {
        //given
        when(productRepository.getProduct(productId)).thenThrow(EmptyResultDataAccessException.class);
        //then
        assertThrows(EmptyResultDataAccessException.class, () -> productService.getProductAmount(request));
    }

}