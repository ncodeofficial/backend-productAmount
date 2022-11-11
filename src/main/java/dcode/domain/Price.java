package dcode.domain;

import dcode.constant.PromotionType;
import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;

public class Price {
    private static final int MIN_PRICE = 10000;
    private static final int PRICE_WON = 1000;
    private final int originPrice;

    private int discountPrice; //총 할인 금액
    private int finalPrice; //확정 상품 가격

    public Price(int originPrice) {
        if (originPrice <= 0) {
            throw new IllegalArgumentException("금액은 0보다 작을수 없습니다.");
        }
        this.originPrice = originPrice;
        this.finalPrice = originPrice;
    }
    public void calculatePrice(Promotion promotion) {
        PromotionType promotionType = PromotionType.getExpression(promotion.getPromotion_type());
        int tempPrice = promotionType.calculate(finalPrice, promotion.getDiscount_value());
        if (tempPrice < MIN_PRICE) {
            throw new IllegalArgumentException("할인된 최소 상품가격은 10,000 이상어이야 합니다.");
        }
        finalPrice = tempPrice;
    }

    private int priceRoundWon(int price) {
        return price / PRICE_WON * PRICE_WON;
    }

    public int getOriginPrice() {
        return originPrice;
    }

    public int getDiscountPrice() {
        return originPrice - priceRoundWon(finalPrice);
    }

    public int getFinalPrice() {
        return priceRoundWon(finalPrice);
    }
}
