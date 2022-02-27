package dcode.service;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request){
        System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");
        
        Product product = repository.getProduct(request.getProductId());
        Promotion promotion = repository.getPromotion(request.getCouponIds());
        int price = product.getPrice();
        int finalPrice = 0;
        int discount = promotion.getDiscount_value();
        
        	if (promotion.getPromotion_type().equals("CODE")) {
        		double code = discount / (double)100;
        		discount = (int) (price * code);
        		finalPrice = (int) (price - discount);
        	}else {
        		finalPrice = price - discount;
        	}        
        	
		if(finalPrice >= 10000) {
			finalPrice = (int) Math.round(finalPrice / 1000.0) * 1000; 
        
        	return ProductAmountResponse.builder()
                	.name(product.getName())
                	.originPrice(price)
                	.discountPrice(discount)
                	.finalPrice(finalPrice)
                	.build();
        	
        }else {
        	System.out.println("할인된 최소 상품가격은 10,000 이상어이야 합니다.");
        	return null;
        	
        }
        
    }

}
