package dcode.product;

import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Test
    public void 가격조회_쿠폰적용() {
        // given
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{3,4})
                .build();

        // when
        int origin_price = 215000;
        int new_price = 152000;

        // then
        ProductAmountResponse response = productService.getProductAmount(request);

        assertThat(response.getOriginPrice()).isEqualTo(origin_price);
        assertThat(response.getFinalPrice()).isEqualTo(new_price);
    }

    @Test
    void 가격조회_쿠폰미적용() {
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(2)
                .couponIds(new int[]{})
                .build();

        ProductAmountResponse response = productService.getProductAmount(request);

        assertThat(response.getOriginPrice()).isEqualTo(35000);
        assertThat(response.getFinalPrice()).isEqualTo(35000);
    }

}
