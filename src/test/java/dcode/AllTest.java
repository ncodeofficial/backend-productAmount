package dcode;

import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AllTest {

    @Autowired
    ProductService productService;

    @Test
    void serviceTest() {
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{1})
                .build();
        ProductAmountResponse response = productService.getProductAmount(request);
    }
}
