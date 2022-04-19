package dcode.exception;

public class UnavailableCouponException extends RuntimeException {
    private static final String MESSAGE = "번 쿠폰은 사용할수 없습니다.";
    public UnavailableCouponException(int couponId) {
        super(couponId + MESSAGE);
    }
}
