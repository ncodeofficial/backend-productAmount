package dcode.repository;

import dcode.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
public class ProductRepository {

    private final EntityManager em;
        public Product getProduct(int id){
        Product product = em.find(Product.class,id);
        return product;
    }

}
