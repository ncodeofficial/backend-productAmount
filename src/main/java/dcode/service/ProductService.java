package dcode.service;

import dcode.domain.entity.Product;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request){
        System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");

        Product product = repository.getProduct(request.getProductId());

        return null;
    }
}
