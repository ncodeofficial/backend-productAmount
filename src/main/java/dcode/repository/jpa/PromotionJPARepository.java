package dcode.repository.jpa;

import dcode.domain.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PromotionJPARepository extends JpaRepository <Promotion, Integer> {
}
