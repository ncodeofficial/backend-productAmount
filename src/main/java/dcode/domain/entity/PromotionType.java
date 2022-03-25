package dcode.domain.entity;

public enum PromotionType {

  COUPON("COUPON") {
    @Override
    public int getDiscountedPrice(int originalPrice, int rate) {
      return originalPrice - rate;
    }
  },
  CODE("CODE") {
    @Override
    public int getDiscountedPrice(int originalPrice, int rate) {
      return (originalPrice / 100) * rate;
    }
  };

  private String type;

  public abstract int getDiscountedPrice(int originalPrice, int rate);

  PromotionType(String type) {
    this.type = type;
  }
}
