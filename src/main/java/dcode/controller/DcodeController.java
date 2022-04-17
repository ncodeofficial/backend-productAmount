package dcode.controller;

import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dcode")
public class DcodeController {

    private final ProductService service;

    //상품 가격 추출 api
    @GetMapping("/product-amount/{productId}")
    public ResponseEntity<ProductAmountResponse> getProductAmount(@PathVariable Integer productId) {

        ProductAmountResponse response = service.getProductAmount(productId);

        // return new ResponseEntity<>(response, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    private ProductInfoRequest getParam(){
        List<Integer> couponIds = Arrays.asList(1, 2);

        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(couponIds)
                .build();

        return request;
    }
}
