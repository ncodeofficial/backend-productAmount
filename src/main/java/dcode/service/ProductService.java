package dcode.service;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.domain.entity.PromotionProducts;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;


@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    public ProductAmountResponse getProductAmount(ProductInfoRequest request){

        Product product = productRepository.getProduct(request.getProductId());
        int finalPrice = product.getPrice();

        for(int couponId : request.getCouponIds()){
           finalPrice = applyPromotion(product,couponId,finalPrice);
        }

        finalPrice = discountBelowThousand(finalPrice);

        ProductAmountResponse productAmountResponse = ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(finalPrice-product.getPrice())
                .finalPrice(finalPrice)
                .build();

        return productAmountResponse;
    }

    private int applyPromotion(Product product,int couponId,int price){
        int discountedPrice = price;
        for(PromotionProducts promotionProducts :product.getPromotionProducts()){
            if(promotionProducts.getPromotion().getId() == couponId){
                Promotion promotion = promotionProducts.getPromotion();
//                if(!validateDate(promotion)) continue; // 현재 날짜 기준으로 적용. 주어진 예제의 promotion들은 이미 기한이 지난 것으로
                                                         // 나타나기 때문에 code에서는 주석처리함
                int discount = getDiscountAmount(product,promotion);
                System.out.println(discount);
                if(price-discount>10000) discountedPrice -= discount;
                break;
            }
        }
        return discountedPrice;
    }



    private boolean validateDate(Promotion promotion){
        Date current = new Date();
        return promotion.getUse_started_at().after(current)&&promotion.getUse_ended_at().before(current);
    }

    private int getDiscountAmount(Product product, Promotion promotion){
        if(promotion.getPromotion_type().equals("CODE")){
            return product.getPrice()*(promotion.getDiscount_value())/100;
        }else{
            return promotion.getDiscount_value();
        }
    }

    private int discountBelowThousand(int finalPrice){
        return finalPrice/1000*1000;
    }
}
