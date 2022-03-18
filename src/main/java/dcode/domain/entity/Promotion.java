package dcode.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Promotion {
    private int id;
    private String promotion_type; //쿠폰 타입 (쿠폰, 코드)
    private String name;
    private String discount_type; // WON : 금액 할인, PERCENT : %할인
    private int discount_value; // 할인 금액 or 할인 %
    private Date use_started_at; // 쿠폰 사용가능 시작 기간
    private Date use_ended_at; // 쿠폰 사용가능 종료 기간

    //사용가능한 쿠폰인지
    public boolean isAvailable(Date now) {
        if (now.compareTo(getUse_started_at()) > 0 && now.compareTo(getUse_ended_at()) < 0) {
            return true;
        }
        return false;
    }

    //할인금액 계산
    public int calcDiscountPrice(int originPrice) {
        if ("WON".equals(getDiscount_type())) {
            return discount_value;
        } else if ("PERCENT".equals(getDiscount_type())) {
            return (int) (originPrice * getDiscount_value() * 0.01);
        }

        return 0;
    }
}
