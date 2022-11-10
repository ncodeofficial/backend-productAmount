package dcode.domain;

import java.util.List;

public class Promotions {
    private final List<Integer> promotionList;

    public Promotions(List<Integer> promotionList) {
        if (promotionList.size() == 0 || promotionList.isEmpty()) {
            throw new IllegalArgumentException("적용 가능한 프로모션이 없습니다");
        }
        this.promotionList = promotionList;
    }

    public boolean isApplicableCoupon(List<Integer> coupon) {
        return promotionList.containsAll(coupon);
    }
}
