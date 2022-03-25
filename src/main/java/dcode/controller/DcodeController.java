package dcode.controller;

import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dcode")
public class DcodeController {

    private final ProductService service;

    //상품 가격 추출 api
    @GetMapping("/product-amount/{id}")
    public ResponseEntity<ProductAmountResponse> getProductAmount(@PathVariable int id) {

        ProductAmountResponse response = service.getProductAmount(getParam(id));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ProductInfoRequest getParam(int id){
        int[] couponIds = {1,2};

        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(id)
                .couponIds(couponIds)
                .build();

        return request;
    }
}
