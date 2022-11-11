package dcode.controller;

import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dcode")
@Slf4j
public class DcodeController {

    private final ProductService service;

    //상품 가격 추출 api
    @GetMapping("/product-amount")
    public ResponseEntity<?> getProductAmount(@Valid ProductInfoRequest productInfoRequest) {
       return service.getProductAmount(getParam());
//        api 호출 테스트용
//        log.info("request ::: {}", productInfoRequest);
//        return service.getProductAmount(productInfoRequest);
    }

    private ProductInfoRequest getParam() {
        int[] couponIds = {1, 2};

        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(couponIds)
                .build();

        return request;
    }
}
