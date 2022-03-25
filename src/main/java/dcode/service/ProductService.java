package dcode.service;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
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
        int discountedPrice = 0;
        Product product = repository.getProduct(request.getProductId());

        for (int id: request.getCouponIds()) {
            Promotion promotion = repository.getPromotion(id);
            String promotionType = promotion.getPromotion_type();
            int discountValue = promotion.getDiscount_value();

            if (promotionType.equals("COUPON")) {
                discountedPrice += discountValue;
            } else if (promotionType.equals("CODE")) {
                discountedPrice += product.getPrice() * discountValue / 100;
            }
        }

        ProductAmountResponse productAmountResponse = ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(discountedPrice)
                .finalPrice(product.getPrice() - discountedPrice)
                .build();
        return productAmountResponse;
    }
}
