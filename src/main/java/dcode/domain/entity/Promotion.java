package dcode.domain.entity;

import dcode.config.PromotionProperties;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

import static dcode.config.PromotionProperties.MIN_FINAL_DISCOUNTED_PRICE;
import static dcode.config.PromotionProperties.MIN_PRICE_UNIT;

@Data
@Builder
public class Promotion {
    private int id;
    private String promotion_type; //쿠폰 타입 (쿠폰, 코드)
    private String name;
    private DiscountType discount_type; // WON : 금액 할인, PERCENT : %할인
    private int discount_value; // 할인 금액 or 할인 %
    private Date use_started_at; // 쿠폰 사용가능 시작 기간
    private Date use_ended_at; // 쿠폰 사용가능 종료 기간


    public int applyTo(int price) {
      if (isExpired() && !isStarted()) return price;

      int discountedPrice =  discount_type.getDiscountedPrice(price, discount_value);

     discountedPrice = (discountedPrice/ MIN_PRICE_UNIT) * MIN_PRICE_UNIT;
      return Math.max(discountedPrice, MIN_FINAL_DISCOUNTED_PRICE);
    }

    private boolean isStarted() {
      return use_started_at.after(new Date());
    }

    private boolean isExpired() {
      return use_ended_at.before(new Date());
    }
}
