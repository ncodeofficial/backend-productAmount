package dcode.service;

import dcode.domain.Discount;
import dcode.domain.Promotions;
import dcode.domain.entity.Product;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import dcode.repository.PromotionProductsRepository;
import dcode.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public ProductAmountResponse getProductAmount(@Valid ProductInfoRequest request) {
        log.info("request ::: {}", request);
        Product product = productRepository.getProduct(request.getProductId());

        if (request.getCouponIds().length > 0) {
            Promotions promotions = new Promotions(promotionProductsRepository.getPromotionProducts(product.getId()));

            List<Integer> coupons = Arrays.stream(request.getCouponIds()).boxed().collect(Collectors.toList());

            if (promotions.isApplicableCoupon(coupons)) {
                Discount discount = new Discount(promotionRepository.getPromotions(coupons), coupons.size());
                int result = discount.calculate(product);
                log.info("result ::: {}", result);
            }
        }

        return null;
    }
}
