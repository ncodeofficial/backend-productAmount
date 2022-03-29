package dcode.service;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import dcode.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;
    private final PromotionRepository promotionRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");

        Product product = repository.getProduct(request.getProductId());
        Optional<Promotion> prm;
        Promotion promotion;
        
        int new_price = product.getPrice();
        int[] coupons = request.getCouponIds();
        
        // 쿠폰이 존재한다면
        if(coupons.length > 0) {
            for (int coupon : coupons) {
                prm = promotionRepository.findById(coupon);
                promotion = prm.get();

                if (promotion.getPromotion_type().equals("COUPON")) {
                    if(promotion.isUsable(new Date())) new_price -= promotion.getDiscount_value();
                    else throw new IllegalArgumentException("사용할 수 없는 쿠폰입니다.");
                } else if (promotion.getPromotion_type().equals("CODE")) {
                    if(promotion.isUsable(new Date())) new_price -= product.getPrice() * promotion.getDiscount_value() / 100;
                    else throw new IllegalArgumentException("사용할 수 없는 쿠폰입니다.");
                }
                if(isPayable(new_price)) throw new IllegalStateException("최소 주문 금액은 만원입니다.");
            }
        }

        // 최종금액 천단위 절삭
        new_price = roundDown1000(new_price);
        return ProductAmountResponse.builder()
                        .name(product.getName())
                        .originPrice(product.getPrice())
                        .discountPrice(product.getPrice() - new_price)
                        .finalPrice(new_price)
                        .build();
    }
    // 주문 가능 여부
    private boolean isPayable(int price) {
        // 최소 주문금액은 만원
        // 만원 미만이면 적용 안됨
        return price < 10000;
    }

    // 천원 절삭
    private int roundDown1000(int price) {
        BigDecimal num = new BigDecimal (price);
        num = num.setScale(-3, RoundingMode.DOWN);
        return num.intValue();
    }

}
