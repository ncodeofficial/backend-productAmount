package dcode.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class PromotionTest {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Promotion promotion;

    @BeforeEach
    public void init() {
        promotion = Promotion.builder()
                .id(1)
                .use_started_at(LocalDate.parse("2022-02-01", formatter))
                .use_ended_at(LocalDate.parse("2022-03-01", formatter))
                .build();
    }

    @Test
    @DisplayName("쿠폰 사용날짜 확인")
    void dateCheck() {
        assertThat(promotion.isUsableCoupon(LocalDate.parse("2022-03-01", formatter))).isTrue();
        assertThat(promotion.isUsableCoupon(LocalDate.parse("2022-02-01", formatter))).isTrue();
        assertThat(promotion.isUsableCoupon(LocalDate.parse("2022-05-01", formatter))).isFalse();

    }
}