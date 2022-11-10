package dcode.service;

import dcode.model.request.ProductInfoRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ProductServiceTest {
    @Autowired
    ProductService productService;

    @Test
    void test() {
        int[] couponIds = {1,2};

        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(couponIds)
                .build();

        productService.getProductAmount(request);
    }
}