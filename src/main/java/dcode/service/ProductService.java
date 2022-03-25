package dcode.service;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.exception.InvalidRequestPropertyException;
import dcode.exception.NoSuchProductException;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductRepository;
import dcode.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    public static final Validator validator =
      Validation.buildDefaultValidatorFactory().getValidator();
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request){
        var violiations = validator.validate(request);
        if (violiations.size()> 0) throw new InvalidRequestPropertyException(violiations.toString());

        Product originalProduct = productRepository.getProduct(request.getProductId()).orElseThrow(NoSuchProductException::new);
        List<Promotion> promotions = promotionRepository.getPromotionsByProductIdAndPromotionId(originalProduct.getId(), request.getPromotionIds());
        Product discountedProduct = originalProduct.deepCopy();
        discountedProduct.applyPromotions(promotions);

        return ProductAmountResponse.builder()
          .name(discountedProduct.getName())
          .originPrice(originalProduct.getPrice())
          .finalPrice(discountedProduct.getPrice())
          .discountPrice(originalProduct.getPrice() - discountedProduct.getPrice())
          .build();
    }
}
