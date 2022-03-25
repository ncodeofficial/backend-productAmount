package dcode.domain.entity;

import dcode.config.PromotionProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static dcode.config.PromotionProperties.MIN_FINAL_DISCOUNTED_PRICE;

@Data
@Builder
public class Product {
    private int id;
    private String name;
    private int price;

    public void applyPromotions(List<Promotion> promotions){
        int totalDiscountAmount = 0;
        for (Promotion promotion :promotions) {
            totalDiscountAmount += promotion.getDiscountedAmount(price);
        }
        price = Math.max(MIN_FINAL_DISCOUNTED_PRICE, price - totalDiscountAmount);
    }

    public Product deepCopy() {
       return Product.builder()
          .id(id)
          .name(name)
          .price(price)
          .build();
    }
}
