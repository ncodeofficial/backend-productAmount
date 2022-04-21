package dcode.exception;

public class MinimumPriceException extends RuntimeException {
    private static final String MESSAGE = "할인된 최소 상품가격은 10,000 이상어이야 합니다.";
    public MinimumPriceException() {
        super(MESSAGE);
    }
}
