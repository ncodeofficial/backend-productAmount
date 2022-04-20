package dcode.service;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import dcode.repository.PromotionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {

    public static final int MIN_PRODUCT_PRICE = 10000; // 최소 상품 금액
    public static final int CUT_PRICE_UNIT = 1000; // 최종 상품 금액 천단위 절삭

    private final ProductRepository productRepository;
    private final PromotionJpaRepository promotionJPARepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        int productId = request.getProductId();
        Product product = productRepository.getProduct(productId);
        int originalPrice = product.getPrice();
        int finalPrice = product.getPrice();

        for (int promotionId : request.getPromotionIds()) {
            Promotion promotion = promotionJPARepository.findPromotion(productId, promotionId);
            if (promotion != null) {
                // 쿠폰 사용 가능한지 검증 후 할인 금액 계산
                finalPrice -= promotion.discountPrice(originalPrice);
            }
        }

        // 할인 적용 됐다면 할인된 최소 상품 금액 10000원 이상, 최종 상품 금액 천단위 절삭
        if (originalPrice != finalPrice)
            finalPrice = (finalPrice <= MIN_PRODUCT_PRICE) ? MIN_PRODUCT_PRICE : (finalPrice / CUT_PRICE_UNIT) * CUT_PRICE_UNIT;

        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(originalPrice)
                .discountPrice(originalPrice - finalPrice)
                .finalPrice(finalPrice)
                .build();
    }
}
