package dcode.service;

import dcode.model.request.ProductInfoRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.UnexpectedTypeException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ProductServiceTest {
    @Autowired
    ProductService productService;

    @Test
    void validCheck() {
        int[] couponIds = {1, 2};
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(0)
                .couponIds(couponIds)
                .build();
        assertThatThrownBy(() -> {
            productService.getProductAmount(request);
        }).isInstanceOf(UnexpectedTypeException.class);

    }

    @Test
    void test() {
        int[] couponIds = {1, 2};
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(3)
                .couponIds(couponIds)
                .build();
        System.out.println(request);
            productService.getProductAmount(request);
    }
}