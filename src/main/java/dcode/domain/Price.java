package dcode.domain;

public class Price {
    private int discountPrice; //총 할인 금액
    private int finalPrice; //확정 상품 가격

    public Price(int originPrice, int finalPrice) {
        this.discountPrice = originPrice - finalPrice;
        this.finalPrice = finalPrice;
    }
}
