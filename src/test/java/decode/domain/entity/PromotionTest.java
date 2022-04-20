package decode.domain.entity;

import dcode.domain.entity.Promotion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static dcode.domain.entity.DiscountType.WON;
import static dcode.domain.entity.PromotionType.CODE;
import static dcode.domain.entity.PromotionType.COUPON;
import static org.assertj.core.api.Assertions.assertThat;

class PromotionTest {

    int productPrice = 10000;
    LocalDate now = LocalDate.now();

    @Test
    @DisplayName("COUPON 할인 계산 성공")
    void discountPriceSuccess() {
        //given
        Promotion promotion = getDiscountPromotionBuilder().promotion_type(COUPON).discount_type(WON).discount_value(1000).build();
        //when
        int discountPrice = promotion.discountPrice(productPrice);
        //then
        assertThat(discountPrice).isEqualTo(1000);
    }

    @Test
    @DisplayName("CODE 할인 계산 성공")
    void discountPriceSuccess2() {
        //given
        Promotion promotion = getDiscountPromotionBuilder().promotion_type(CODE).discount_type(WON).discount_value(10).build();
        //when
        int discountPrice = promotion.discountPrice(productPrice);
        //then
        assertThat(discountPrice).isEqualTo(1000);
    }

    @Test
    @DisplayName("프로모션 사용 가능")
    void useValidCheckSuccess() {
        //given
        Promotion promotion = getUseValidCheckBuilder().use_started_at(now.minusDays(1)).use_ended_at(now.plusDays(1)).build();
        //then
        assertThat(promotion.useValidCheck()).isTrue();
    }

    @Test
    @DisplayName("프로모션 사용 불가능 - 시작일 안지남")
    void useValidCheckFail() {
        //given
        Promotion promotion = getUseValidCheckBuilder().use_started_at(now.plusDays(1)).use_ended_at(now.plusDays(2)).build();
        //then
        assertThat(promotion.useValidCheck()).isFalse();
    }

    @Test
    @DisplayName("프로모션 사용 불가능 - 종료일 지남")
    void useValidCheckFail2() {
        //given
        Promotion promotion = getUseValidCheckBuilder().use_started_at(now.minusDays(2)).use_ended_at(now.minusDays(1)).build();
        //then
        assertThat(promotion.useValidCheck()).isFalse();
    }

    // 할인 계산 확인용 Promotion.builder()
    Promotion.PromotionBuilder getDiscountPromotionBuilder() {
        return Promotion.builder()
                .id(1).
                name("상품명")
                .use_started_at(now.minusDays(1))
                .use_ended_at(now.plusDays(1));
    }

    // 사용 가능한 프로모션 확인용 Promotion.builder()
    Promotion.PromotionBuilder getUseValidCheckBuilder() {
        return Promotion.builder()
                .id(1)
                .name("상품명")
                .promotion_type(COUPON)
                .discount_type(WON)
                .discount_value(1000);
    }
}