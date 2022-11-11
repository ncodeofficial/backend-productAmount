package dcode.domain;

import dcode.domain.entity.Promotion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class PriceTest {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    Promotion promotion;

    @BeforeEach
    void init() {
        promotion = Promotion.builder()
                .id(1)
                .promotion_type("COUPON")
                .name("30000원 할인쿠폰")
                .discount_type("WON")
                .discount_value(30000)
                .use_started_at(LocalDate.parse("2022-11-01", formatter))
                .use_ended_at(LocalDate.parse("2022-12-01", formatter)).build();
    }

    @Test
    void 금액_확인() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Price(-1));
        assertThatIllegalArgumentException().isThrownBy(() -> new Price(0));
        assertThat(new Price(1).getOriginPrice()).isEqualTo(1);
    }

    @Test
    void 계산_확인() {
        Price testPrice1 = new Price(10000);
        assertThatIllegalArgumentException().isThrownBy(() -> testPrice1.calculatePrice(promotion));

        Price testPrice2 = new Price(30000);
        assertThatIllegalArgumentException().isThrownBy(() -> testPrice2.calculatePrice(promotion));

        Price testPrice3 = new Price(50000);
        testPrice3.calculatePrice(promotion);
        assertThat(testPrice3.getFinalPrice()).isEqualTo(20000);
    }
}