package dcode.constant;

import java.util.Arrays;
import java.util.function.BiFunction;

public enum PromotionType {
    COUPON("COUPON", (price, discount) -> price - discount),
    CODE("CODE", (price, discount) -> price - (price * discount / 100) );

    private final String promotionType;
    private final BiFunction<Integer, Integer, Integer> expression;

    PromotionType(String promotionType, BiFunction<Integer, Integer, Integer> expression) {
        this.promotionType = promotionType;
        this.expression = expression;
    }

    public int calculate(int price, int discount) {
        return expression.apply(price, discount);
    }

    public static PromotionType getExpression(String promotionType) {
        return Arrays.stream(values())
                .filter(value -> value.promotionType.equals(promotionType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 프로모션입니다."));
    }
}
