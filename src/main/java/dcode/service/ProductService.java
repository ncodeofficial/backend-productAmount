package dcode.service;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.domain.entity.PromotionProducts;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request){
        System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");

        Product product = repository.getProduct(request.getProductId());
        int discountedPrice = 0;
        int price = product.getPrice();

        // 상품할인 적용
        for (int id: request.getCouponIds()) {
            PromotionProducts promotionProducts = repository.getPromotionProductes(id, product.getId());

            // 쿠폰이 적용되는지 검증로직
            if (promotionProducts != null) {
                Promotion promotion = repository.getPromotion(id);
                String promotionType = promotion.getPromotion_type();
                int discountValue = promotion.getDiscount_value();

                if (promotionType.equals("COUPON")) {
                    discountedPrice += discountValue;
                } else if (promotionType.equals("CODE")) {
                    discountedPrice += price * discountValue / 100;
                }
            }
        }
        int finalPrice = price - discountedPrice;

        // 천단위 절삭
        finalPrice = finalPrice / 1000 * 1000;

        // 최소 상품가격은 만원
        if (finalPrice < 10000) finalPrice = 10000;

        // 할인가격 조정
        discountedPrice = price - finalPrice;

        ProductAmountResponse productAmountResponse = ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(price)
                .discountPrice(discountedPrice)
                .finalPrice(finalPrice)
                .build();
        return productAmountResponse;
    }
}

/*
* 215000
* 62250
* 152000
 */