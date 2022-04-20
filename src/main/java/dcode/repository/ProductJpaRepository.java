package dcode.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dcode.domain.entity.Product;

public interface ProductJpaRepository extends JpaRepository<Product, Integer> {
}
