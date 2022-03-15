package dcode.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductAmountResponse {
    private String name; // 상품명

    private int originPrice; // 상품 기존 가격
    private int discountPrice; // 총 할인 금액
    private int finalPrice; // 확정 상품 가격
    
    private int productId; // 상품아이디
    private int[] couponIds; // 입력된 쿠폰
    private int[] invalidCouponIds; // 유효하지 않은 쿠폰(유효기간 아님, id 없음)
    private String errorMessage; // 에러 메시지
}
