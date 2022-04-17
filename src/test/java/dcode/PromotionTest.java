package dcode;

import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
public class PromotionTest {

    @Autowired
    ProductService productService;

    @Test
    public void rightlyOrdered(){
        int[] couponIds = new int[]{1,2};
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(couponIds)
                .build();

        ProductAmountResponse productAmountResponse = productService.getProductAmount(request);
        System.out.println(productAmountResponse.getFinalPrice());
    }

    @Test
    public void wrongCouponOrdered(){
        int[] couponIds = new int[]{3,4};
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(couponIds)
                .build();

        ProductAmountResponse productAmountResponse = productService.getProductAmount(request);
        System.out.println(productAmountResponse.getFinalPrice());
    }
}
