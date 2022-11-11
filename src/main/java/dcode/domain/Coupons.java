package dcode.domain;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Slf4j
public class Coupons {
    private final List<Promotion> couponList;

    public Coupons(List<Promotion> couponList, int size) {
        if (couponList.size() != size) {
            throw new IllegalArgumentException("사용할 수 없는 쿠폰이 존재합니다");
        }
        this.couponList = couponList;
    }

    public Price calculate(Product product) {
        Price price = new Price(product.getPrice());
        for (Promotion promotion : couponList) {
            validCoupon(promotion,price);
        }
        return price;
    }

    private Price validCoupon(Promotion promotion, Price price) {
        if (!promotion.isUsableCoupon(getDateNow())) {
            throw new IllegalArgumentException("쿠폰기간이 잘못되었습니다.");
        }
        price.calculatePrice(promotion);
        return price;
    }

    private LocalDate getDateNow() {
        LocalDate SeoulNow = LocalDate.now(ZoneId.of("Asia/Seoul"));
        return SeoulNow;
    }
}
