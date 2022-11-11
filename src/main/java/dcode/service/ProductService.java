package dcode.service;

import dcode.domain.Discount;
import dcode.domain.Price;
import dcode.domain.Promotions;
import dcode.domain.entity.Product;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import dcode.repository.PromotionProductsRepository;
import dcode.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Validated
public class ProductService {
    private final ProductRepository productRepository;
    private final PromotionProductsRepository promotionProductsRepository;
    private final PromotionRepository promotionRepository;

    public ResponseEntity<ProductAmountResponse> getProductAmount(@Valid ProductInfoRequest request) {
        Product product = productRepository.getProduct(request.getProductId());
        if (product == null) {
            return new ResponseEntity("사용자를 찾을수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        if (request.getCouponIds().length > 0) {
            try {
                List<Integer> coupons = Arrays.stream(request.getCouponIds()).boxed().collect(Collectors.toList());

                Promotions promotions = new Promotions(promotionProductsRepository.getPromotionProducts(product.getId()));

                if (promotions.isApplicableCoupon(coupons)) {
                    Discount discount = new Discount(promotionRepository.getPromotions(coupons), coupons.size());
                    Price price = discount.calculate(product);
                    log.info("price ::: {}", price);
                    return new ResponseEntity<>(ProductAmountResponse.builder()
                            .name(product.getName())
                            .originPrice(price.getOriginPrice())
                            .discountPrice(price.getDiscountPrice())
                            .finalPrice(price.getFinalPrice())
                            .build(), HttpStatus.OK);

                }
            }catch (Exception e){
                return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

        }

        return new ResponseEntity<>(ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .build(), HttpStatus.OK);
    }
}
