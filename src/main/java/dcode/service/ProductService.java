package dcode.service;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.domain.entity.PromotionProducts;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import dcode.repository.PromotionProductsRepository;
import dcode.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;
    private final PromotionRepository promotionRepository;
    private final PromotionProductsRepository promotionProductsRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request){
        System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");

        Product product = repository.getProduct(request.getProductId());    // request로 넘어온 상품 정보
        List<Promotion> promotionList = new ArrayList<Promotion>(); //  가능한 promotion을 담을 리스트

        // 쿠폰 아이디랑 상품 아이디로 해당 할인이 존재 하면 promotionList에 담기
        for (int couponId: request.getCouponIds()) {
            PromotionProducts promotionProducts = promotionProductsRepository.findByPromotionIdAndProductId(couponId, request.getProductId()).get();
            if (promotionProducts != null) { // 쿠폰 ID와 상품 ID가 일치하는 값이 있는 경우
                promotionList.add(promotionRepository.findById(couponId).get());
            }
        }   // end of for

        int price = product.getPrice();     // 만원 미만으로 떨어지는지 확인하기 위한 변수
        Date now =  new Date();             // 현재 시간

        for (Promotion promotion: promotionList) {  // request 정보로 가능한 Promotion List 전부 적용
            // 쿠폰 사용 가능 날짜 이전이거나 이후면 적용 안함
            if (now.before(promotion.getUse_started_at()) || now.after(promotion.getUse_ended_at())) {
                continue;
            }
            // 프로모션이 쿠폰인지 코드인지 확인 후 맞는 로직으로 처리
            if (promotion.getPromotion_type().equals("COUPON")) {
                price -= promotion.getDiscount_value();
            } else if (promotion.getPromotion_type().equals("CODE")) {
                price -= product.getPrice() * (promotion.getDiscount_value()) / 100;  // 원가에 대해서 할인함
            }
            if (price < 10000) {     // 거래 불가. 할인 할 때마다 계속 확인
                return null;
            }
        }   // end of for

        // 천단위 절삭 ex 13500 -> 13000
        price = (price / 1000) * 1000;
        // Response에 들어갈 정보 저장
        ProductAmountResponse productAmountResponse = ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(product.getPrice() - price)  // 최종 가격에서 할인가 빼줌
                .finalPrice(price)  // 최종 가격
                .build();
        return productAmountResponse;
    }
}
