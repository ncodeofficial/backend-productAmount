package dcode.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dcode.domain.entity.Promotion;

public interface PromotionJpaRepository extends JpaRepository<Promotion, Integer> {
}
