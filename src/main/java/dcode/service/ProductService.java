package dcode.service;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import dcode.repository.PromotionProductRepository;
import dcode.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRrepository;
    private final PromotionRepository promotionRepository;
    private final PromotionProductRepository promotionProductRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        //System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");
        Product product = productRrepository.getProduct(request.getProductId());
        List<Integer> promotionIdList = promotionProductRepository.getPromotionIdList(request.getProductId());
        List<Promotion> promotionList = promotionRepository.getPromotionList(promotionIdList);
        int discountPrice = 0;
        for (Promotion promotion : promotionList) {
            Date promotionStartDate = promotion.getUse_started_at();
            Date promotionEndDate = promotion.getUse_ended_at();
            switch (promotion.getDiscount_type()) {
                case "WON":
                    if (isAvailableCoupon(promotionStartDate, promotionEndDate)) {
                        discountPrice += promotion.getDiscount_value();
                    }
                    break;
                case "PERCENT":
                    if (isAvailableCoupon(promotionStartDate, promotionEndDate)) {
                        discountPrice += (product.getPrice() * promotion.getDiscount_value() / 100);
                    }
                    break;
                default:
                    break;
            }
        }
        ProductAmountResponse response = ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(discountPrice)
                .finalPrice(product.getPrice() - discountPrice)
                .build();
        log.info(">>> response: " + response.toString());
        return response;
    }

    public boolean isAvailableCoupon(Date startDate, Date endDate) {
        Date today = new Date();
        return today.after(startDate) && today.before(endDate);
    }
}
