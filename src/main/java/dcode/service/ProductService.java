package dcode.service;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.exception.ErrorCode;
import dcode.exception.CustomException;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import dcode.repository.PromotionProductRepository;
import dcode.repository.PromotionRepository;
import dcode.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final PromotionProductRepository promotionProductRepository;

    public ProductAmountResponse getProductAmount(Integer productId) {
        //System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");

        Product product = productRepository.getProduct(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "productId", productId.toString()));
        List<Integer> promotionIdList = promotionProductRepository.getPromotionIdList(productId);
        List<Promotion> promotionList = promotionRepository.getPromotionList(promotionIdList);
        int discountPrice = 0;
        for (Promotion promotion : promotionList) {
            Date promotionStartDate = promotion.getUse_started_at();
            Date promotionEndDate = promotion.getUse_ended_at();

            // 쿠폰 사용 가능날짜인지 판단 후 적용
            if (!DateUtil.isAvailableCoupon(promotionStartDate, promotionEndDate)) {
                continue;
            }

            switch (promotion.getDiscount_type()) {
                case "WON":
                    discountPrice += promotion.getDiscount_value();
                    break;
                case "PERCENT":
                    discountPrice += (product.getPrice() * promotion.getDiscount_value() / 100);
                    break;
                default:
                    break;
            }
        }
        int finalPrice;
        // 할인된 최소 상품가격은 10,000 이상
        if (discountPrice < 10000) {
            finalPrice = product.getPrice();
        } else {
            // 최종 할인금액 1000의 자리 절삭
            log.info(">>> 천의자리 절삭 전 : " + (product.getPrice() - discountPrice));
            finalPrice = (product.getPrice() - discountPrice) - ((product.getPrice() - discountPrice) % 10000);
            log.info(">>> 천의자리 절삭 후 : " + finalPrice);
        }

        ProductAmountResponse response = ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(discountPrice)
                .finalPrice(finalPrice)
                .build();
        log.info(">>> response: " + response.toString());
        return response;
    }
}
