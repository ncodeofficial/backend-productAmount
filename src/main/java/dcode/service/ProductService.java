package dcode.service;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.domain.entity.PromotionProducts;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import dcode.repository.PromotionRepository;
import dcode.util.CalcUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request){

        Product product = productRepository.getProduct(request.getProductId());
        List<Promotion> promotions = promotionRepository.getPromotionByProductId(product.getId());

        int discountPrice = 0;
        if(request.getCouponIds() != null) {
            for (int couponId : request.getCouponIds()) {
                Promotion promotion = promotions.stream().filter(p -> p.getId() == couponId)
                        .findAny()
                        .orElse(Promotion.builder().build());

                if (promotion.getId() == 0) {
                    throw new IllegalArgumentException("존재하지 않거나 사용할 수 없는 쿠폰입니다.");
                } else if (promotion.isAvailable(new Date())) {
                    discountPrice += promotion.calcDiscountPrice(product.getPrice());
                } else {
                    throw new IllegalArgumentException("사용할 수 없는 쿠폰입니다.");
                }
            }
        }

        int finalPrice = CalcUtil.roundDown(product.getPrice() - discountPrice, 1000);

        //할인된 최소 상품가격은 10,000 이상
        if (finalPrice < 10000) {
            throw new IllegalStateException("할인된 최소 상품가격은 10,000 이상이어야합니다.");
        }

        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(discountPrice)
                .finalPrice(finalPrice)
                .build();
    }
}
