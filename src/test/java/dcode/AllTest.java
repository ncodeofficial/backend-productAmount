package dcode;

import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.service.ProductService;
import dcode.util.CalcUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AllTest {

    @Autowired
    ProductService productService;

    @Test
    void 상품_가격_조회() {
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{1,2})
                .build();
        ProductAmountResponse response = productService.getProductAmount(request);
        //215000 * 0.85 - 30000 = 152,750 --> 152,000
        assertThat(response.getOriginPrice()).isEqualTo(215000);
        assertThat(response.getFinalPrice()).isEqualTo(152000);
    }

    @Test
    void 상품_가격_조회_쿠폰안씀() {
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(2)
                .build();
        ProductAmountResponse response = productService.getProductAmount(request);

        assertThat(response.getOriginPrice()).isEqualTo(10000);
        assertThat(response.getFinalPrice()).isEqualTo(10000);
    }

    @Test
    void 상품_가격_조회_만원_미만() {
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(2)
                .couponIds(new int[]{1})
                .build();

        assertThrows(IllegalStateException.class, ()-> {
            ProductAmountResponse response = productService.getProductAmount(request);
        });
    }

    @Test
    void 해당_상품에_적용되지않는_쿠폰_사용() {
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(3)
                .couponIds(new int[]{1})
                .build();

        assertThrows(IllegalArgumentException.class, ()-> {
            ProductAmountResponse response = productService.getProductAmount(request);
        });
    }

    @Test
    void 없는_상품_조회() {
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(99)
                .couponIds(new int[]{1})
                .build();
        assertThrows(EmptyResultDataAccessException.class, ()-> {
            ProductAmountResponse response = productService.getProductAmount(request);
        });
    }

    @Test
    void 없는_쿠폰_조회() {
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{1,99})
                .build();
        assertThrows(IllegalArgumentException.class, ()-> {
            ProductAmountResponse response = productService.getProductAmount(request);
        });
    }

    @Test
    void 사용기간지난_쿠폰_조회() {
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{1,4})
                .build();
        assertThrows(IllegalArgumentException.class, ()-> {
            ProductAmountResponse response = productService.getProductAmount(request);
        });
    }

    @Test
    void 사용기간전인_쿠폰_조회() {
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{1,4})
                .build();
        assertThrows(IllegalArgumentException.class, ()-> {
            ProductAmountResponse response = productService.getProductAmount(request);
        });
    }

    @Test
    void roundDown() {
        long result = CalcUtil.roundDown(9999, 1000);
        assertThat(result).isEqualTo(9000);

        long result2 = CalcUtil.roundDown(45890, 100);
        assertThat(result2).isEqualTo(45800);
    }
}
