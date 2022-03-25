package dcode.repository;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.domain.entity.PromotionProducts;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Product getProduct(int id) {
        String query = "SELECT * FROM `product` WHERE id = :id ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return namedParameterJdbcTemplate.queryForObject(
                query,
                params,
                (rs, rowNum) -> Product.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .price(rs.getInt("price"))
                        .build()
        );
    }

    public Promotion getPromotion(int id) {
        String query = "SELECT * FROM `promotion` WHERE id = :id ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return namedParameterJdbcTemplate.queryForObject(
                query,
                params,
                (rs, rowNum) -> Promotion.builder()
                        .id(rs.getInt("id"))
                        .promotion_type(rs.getString("promotion_type"))
                        .name(rs.getString("name"))
                        .discount_type(rs.getString("discount_type"))
                        .discount_value(rs.getInt("discount_value"))
                        .use_started_at(rs.getDate("use_started_at"))
                        .use_ended_at(rs.getDate("use_ended_at"))
                        .build()
        );
    }

    public PromotionProducts getPromotionProductes(int promotionId, int productId) {
        String query = "SELECT * FROM `promotion_products` WHERE promotion_id = :promotionId AND product_id = :productId ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("promotionId", promotionId);
        params.addValue("productId", productId);

        // 할인적용상품이 아닌경우 null 을 반환
        try {
            PromotionProducts promotionProducts = namedParameterJdbcTemplate.queryForObject(
                    query,
                    params,
                    (rs, rowNum) -> PromotionProducts.builder()
                            .id(rs.getInt("id"))
                            .promotionId(rs.getInt("promotion_id"))
                            .productId(rs.getInt("product_id"))
                            .build()
            );
            return promotionProducts;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
