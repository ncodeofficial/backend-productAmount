package dcode.service;

import dcode.domain.Coupons;
import dcode.domain.Price;
import dcode.domain.Promotions;
import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.jpa.ProductJPARepository;
import dcode.repository.jpa.PromotionJPARepository;
import dcode.repository.jpa.PromotionProductsJPARepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Validated
@Transactional(readOnly = true)
public class ProductService {
    private final ProductJPARepository productJPARepository;
    private final PromotionJPARepository promotionJPARepository;
    private final PromotionProductsJPARepository promotionProductsJPARepository;

    public ResponseEntity<?> getProductAmount(@Valid ProductInfoRequest request) {
        Product product = productJPARepository.findById(request.getProductId()).orElseThrow(() -> {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        });
        try {
            if (request.getCouponIds() != null && request.getCouponIds().length > 0) {
                List<Integer> couponIds = Arrays.stream(request.getCouponIds()).boxed().collect(Collectors.toList());

                Promotions productPromotions = new Promotions(
                        promotionProductsJPARepository.findAllByProductId(product.getId())
                                .orElseThrow(
                                        () -> {
                                            throw new IllegalArgumentException("관련된 프로모션을 찾을 수 없습니다.");
                                        }
                                ));

                if (productPromotions.isApplicableCoupon(couponIds)) {
                    List<Promotion> promotions = promotionJPARepository.findAllById(couponIds);
                    if (promotions.size() > 0) {
                        Coupons coupons = new Coupons(promotions, couponIds.size());
                        Price price = coupons.calculate(product);

                        ProductAmountResponse result = ProductAmountResponse.builder()
                                .name(product.getName())
                                .originPrice(price.getOriginPrice())
                                .discountPrice(price.getDiscountPrice())
                                .finalPrice(price.getFinalPrice())
                                .build();

                        log.info("discount result ::: {}", result);

                        return new ResponseEntity<>(result, HttpStatus.OK);

                    }
                }
                throw new IllegalArgumentException("쿠폰에 해당하는 프로모션을 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        ProductAmountResponse result = ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .build();

        log.info("default result ::: {}", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
