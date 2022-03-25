package dcode.domain.entity;

public enum DiscountType {

  WON("WON") {
    @Override
    public int getDiscountedPrice(int originalPrice, int rate) {
      return originalPrice - rate;
    }
  },

  PERCENT("PERCENT") {
    @Override
    public int getDiscountedPrice(int originalPrice, int rate) {
      return (originalPrice / 100) * rate;
    }
  };

  private String type;

  public abstract int getDiscountedPrice(int originalPrice, int rate);

  DiscountType(String type) {
    this.type = type;
  }
}
