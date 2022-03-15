package dcode.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request){
        System.out.println("product: " + request.getProductId());
        System.out.println("coupon: " + Arrays.toString(request.getCouponIds()));
        
        Product product = repository.getProduct(request.getProductId());
        if(product==null) {
        	return ProductAmountResponse.builder()
        			.productId(request.getProductId())
        			.errorMessage("상품정보가 확인되지 않습니다. 관리자에게 문의해주세요.")
        			.build();
        }
        
        System.out.println("product select success");
        
        int originPrice = product.getPrice();
        System.out.println("orgPrice: " + originPrice);
        
        // 입력된 쿠폰이 없는 경우 = 일반 판매
        if(request.getCouponIds().length == 0) {
        	return ProductAmountResponse.builder()
        			.productId(request.getProductId())
        			.name(product.getName())
        			.originPrice(originPrice)
        			.finalPrice(originPrice)
        			.build();
        }
        
        List<Promotion> promotionList = repository.getPromotion(request.getCouponIds());
        System.out.println("coupon select success");
        
        int finalPrice = originPrice;
        String errorMessage = null;

        // 쿠폰 id 조회 성공
        if(!promotionList.isEmpty()) {
        	
        	// 쿠폰 일부만 조회된 경우, 조회되지 않은 쿠폰 정보 저장
        	int[] invalidCouponIds = null;
        	List<Integer> inputCouponList = Arrays.stream(request.getCouponIds())
        										.boxed()
        										.collect(Collectors.toList());
        	List<Integer> existingCouponList = promotionList
												.stream()
												.mapToInt(Promotion::getId)
												.boxed()
												.collect(Collectors.toList());
        	inputCouponList.removeAll(existingCouponList);
        	if(inputCouponList.size() > 0) {
        		errorMessage = "사용할 수 없는 쿠폰이 있습니다. 확인 후 다시 시도해주세요.";
            	invalidCouponIds = inputCouponList
	    							.stream()
	    							.mapToInt(i->i)
	    							.toArray();
        	}
        	
        	// 계산 시작
            int discountPrice = promotionList
            						.stream()
					            	.filter(pl -> pl.getPromotion_type().equals("COUPON"))
					            	.mapToInt(pl -> pl.getDiscount_value())
					            	.sum();
            System.out.println( "금액 쿠폰으로 할인: " + discountPrice );
            

            int discountPercent = promotionList
            						.stream()
	            					.filter(pl -> pl.getPromotion_type().equals("CODE"))
	            					.mapToInt(pl -> pl.getDiscount_value())
	            					.sum();
            float discountPercentFloat = discountPercent/(float)100;
            System.out.println( "할인율: " + discountPercentFloat );
            
            BigDecimal originPriceMath = new BigDecimal(String.valueOf(originPrice));
            BigDecimal discountPercentMath = new BigDecimal(String.valueOf(discountPercentFloat));

            System.out.println( "퍼센트 쿠폰으로 할인: " + originPriceMath.multiply(discountPercentMath) );
            
            discountPrice += originPriceMath.multiply(discountPercentMath).intValue();
            System.out.println( "총 할인금액: " + discountPrice );
            System.out.println( "할인 후 금액: " + (originPrice - discountPrice));
            
            // 천단위 절사
            BigDecimal finalPriceBase = new BigDecimal (originPrice - discountPrice);
            finalPrice = finalPriceBase.setScale(-4, RoundingMode.DOWN).intValue();
            System.out.println( "할인 후 금액(최종): " + finalPrice );
            
            if(finalPrice < 10000) {
            	errorMessage = "할인 후 금액이 1만원 이상이어야 합니다.";
            }
            
            return ProductAmountResponse.builder()
        			.productId(request.getProductId())
        			.name(product.getName())
        			.originPrice(originPrice)
        			.discountPrice(originPrice - finalPrice)
        			.finalPrice(finalPrice)
        			.couponIds(request.getCouponIds())
        			.invalidCouponIds(invalidCouponIds)
        			.errorMessage(errorMessage) // 일부만 정상 적용된 경우에도 에러 처리
        			.build();
            
        // 쿠폰 id 조회 실패
        }else {
        	errorMessage = "사용할 수 없는 쿠폰입니다. 확인 후 다시 입력해주세요.";
        	return ProductAmountResponse.builder()
        			.productId(request.getProductId())
        			.name(product.getName())
        			.couponIds(request.getCouponIds())
        			.invalidCouponIds(request.getCouponIds())
        			.errorMessage(errorMessage)
        			.build();
        }
    }
}
