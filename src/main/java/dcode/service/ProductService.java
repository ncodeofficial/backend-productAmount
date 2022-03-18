package dcode.service;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import dcode.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request){

        Product product = productRepository.getProduct(request.getProductId());

        int discountPrice = 0;
        for (int couponId : request.getCouponIds()) {
            Promotion promotion = promotionRepository.getPromotion(couponId);
            if (promotion.isAvailable(new Date())) {
                discountPrice += promotion.calcDiscountPrice(product.getPrice());
            } else {
                throw new IllegalArgumentException("사용할 수 없는 쿠폰입니다.");
            }
        }

        int finalPrice = product.getPrice() - discountPrice;

        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(discountPrice)
                .finalPrice(finalPrice)
                .build();
    }
}
