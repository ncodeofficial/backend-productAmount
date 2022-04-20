package dcode.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.domain.entity.PromotionProducts;

public interface PromotionProductsJpaRepository extends JpaRepository<PromotionProducts, Integer> {

	Optional<PromotionProducts> findByProductAndPromotion(Product product, Promotion promotion);
}
