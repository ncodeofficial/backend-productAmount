package dcode.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Product {
    private int id;
    private String name;
    private int price;

    public void applyPromotions(List<Promotion> promotions){
        promotions.stream().forEach(this::applyPromotion);
    }

    private void applyPromotion(Promotion promotion) {
       price = promotion.applyTo(price);
    }

    public Product deepCopy() {
       return Product.builder()
          .id(id)
          .name(name)
          .price(price)
          .build();
    }
}
