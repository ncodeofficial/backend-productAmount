package dcode.repository;

import dcode.domain.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionJpaRepository extends JpaRepository<Promotion, Integer> {

    @Query("SELECT p FROM Promotion p INNER JOIN PromotionProducts pp ON pp.promotion.id =:promotionId AND pp.product.id =:productId AND p.id=pp.id")
    Promotion findPromotion(@Param("productId") int productId, @Param("promotionId") Integer promotionId);

}
