package dcode.domain;

import dcode.constant.PromotionType;
import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Slf4j
public class Discount {
    private static final int MIN_PRICE = 10000;
    private static final int PRICE_WON = 1000;
    private final List<Promotion> discountList;

    public Discount(List<Promotion> discountList, int size) {
        if (discountList.size() != size) {
            throw new IllegalArgumentException("사용할 수 없는 쿠폰이 존재합니다");
        }
        this.discountList = discountList;
    }

    public int calculate(Product product) {
        int beforePrice = product.getPrice();
        for (Promotion promotion : discountList) {
            int afterPrice = validUsableCoupon(promotion, beforePrice);
            if (afterPrice < MIN_PRICE) {
                break;
            }
            beforePrice = afterPrice;
        }
        return priceRoundWon(beforePrice);
    }

    private int validUsableCoupon(Promotion promotion, int beforePrice) {
        if (!promotion.isUsableCoupon(getDateNow())) {
            throw new IllegalArgumentException("쿠폰기간이 잘못되었습니다.");
        }
        return calculatePromotionType(promotion, beforePrice);
    }

    private int calculatePromotionType(Promotion promotion, int beforePrice) {
        PromotionType promotionType = PromotionType.getExpression(promotion.getPromotion_type());
        return promotionType.calculate(beforePrice, promotion.getDiscount_value());
    }

    private int priceRoundWon(int price) {
        return price / PRICE_WON * PRICE_WON;
    }

    private LocalDate getDateNow() {
        LocalDate SeoulNow = LocalDate.now(ZoneId.of("Asia/Seoul"));
        return SeoulNow;
    }
}
