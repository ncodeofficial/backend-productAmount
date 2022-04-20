package dcode.repository;

import dcode.domain.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionJpaRepository extends JpaRepository<Promotion, Integer> {

    @Query("select p from Promotion p where p.id = (select pp.promotion.id from PromotionProducts pp where pp.promotion.id = :promotionId AND pp.product.id = :productId)")
    Promotion findPromotion(@Param("productId") int productId, @Param("promotionId") Integer promotionId);

}
