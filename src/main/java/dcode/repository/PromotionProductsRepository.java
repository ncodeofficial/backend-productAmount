package dcode.repository;

import dcode.domain.entity.PromotionProducts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionProductsRepository extends JpaRepository<PromotionProducts, Integer> {
    Optional<PromotionProducts> findByPromotionIdAndProductId(int promotionId, int productId);

}
