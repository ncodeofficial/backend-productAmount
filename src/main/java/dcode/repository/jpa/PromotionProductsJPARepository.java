package dcode.repository.jpa;

import dcode.domain.entity.PromotionProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PromotionProductsJPARepository extends JpaRepository<PromotionProducts, Integer> {
    @Query(value = "SELECT pp.promotionId FROM PromotionProducts pp WHERE pp.productId = :product_id")
    Optional<List<Integer>> findAllByProductId(@Param("product_id") int productId);
}
