package dcode.service;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.exception.MinimumPriceException;
import dcode.exception.UnavailableCouponException;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import dcode.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request){
        // 사용할수 있는 쿠폰인지 확인
        for (int couponId : request.getCouponIds()) {
            int checkPromotion = promotionRepository.checkPromotion(request.getProductId(), couponId);
            if (checkPromotion == 0) {
                throw new UnavailableCouponException(couponId);
            }
        }
        // 사용할수 있는 쿠폰이므로 상품과 쿠폰 가져온다.
        Product product = productRepository.getProduct(request.getProductId());
        List<Promotion> promotions = promotionRepository.getPromotion(request.getCouponIds());

        //할인 적용
        int originPrice = product.getPrice();
        int discountPrice = discount(product, promotions);
        int finalPrice = originPrice - discountPrice;
        //할인된 최소 상품가격은 10,000 이상어이야 한다.
        if (finalPrice < 10000) {
            throw new MinimumPriceException();
        }
        //천단위는 절삭한다.
        finalPrice = (finalPrice / 1000) * 1000;

        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(originPrice)
                .discountPrice(discountPrice)
                .finalPrice(finalPrice)
                .build();
    }

    private int discount(Product product, List<Promotion> promotions) {
        int discountPrice = 0;

        for (Promotion promotion : promotions) {
            if (promotion.getPromotion_type().equals("COUPON")) {
                discountPrice += promotion.getDiscount_value();
            } else if (promotion.getPromotion_type().equals("CODE")) {
                discountPrice += product.getPrice() * promotion.getDiscount_value()/100;
            }
        }
        return discountPrice;
    }
}
