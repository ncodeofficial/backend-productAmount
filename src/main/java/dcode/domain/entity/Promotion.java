package dcode.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

import static dcode.domain.entity.PromotionType.CODE;
import static dcode.domain.entity.PromotionType.COUPON;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private PromotionType promotion_type; //쿠폰 타입 (쿠폰, 코드)

    private String name;

    @Enumerated(EnumType.STRING)
    private DiscountType discount_type; // WON : 금액 할인, PERCENT : %할인

    private int discount_value; // 할인 금액 or 할인 %

    private LocalDate use_started_at; // 쿠폰 사용가능 시작 기간

    private LocalDate use_ended_at; // 쿠폰 사용가능 종료 기간

    public int discountPrice(int product_price) {
        int discount = 0;
        if (useValidCheck()) { // 쿠폰 사용 가능한지 검증
            if (promotion_type == CODE) {
                discount = (product_price / 100 * discount_value);
            } else if (promotion_type == COUPON) {
                discount = discount_value;
            }
        }
        return discount;
    }

    public Boolean useValidCheck() {
        LocalDate now = LocalDate.now();
        return use_started_at.isBefore(now) && use_ended_at.isAfter(now);
    }
}
