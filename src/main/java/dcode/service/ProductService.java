package dcode.service;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.exception.NoSuchProductException;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import dcode.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public ProductAmountResponse getProductAmount(@Valid ProductInfoRequest request){
        Product originalProduct = productRepository.getProduct(request.getProductId()).orElseThrow(NoSuchProductException::new);
        List<Promotion> promotions = promotionRepository.getPromotionsByProductIdAndPromotionId(originalProduct.getId(), request.getPromotionIds());
        Product newProduct = originalProduct.deepCopy();
        newProduct.applyPromotions(promotions);

        return ProductAmountResponse.builder()
          .name(newProduct.getName())
          .originPrice(originalProduct.getPrice())
          .finalPrice(newProduct.getPrice())
          .discountPrice(newProduct.getPrice() - originalProduct.getPrice())
          .build();
    }
}
