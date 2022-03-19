package dcode.repository;

import dcode.domain.entity.Promotion;
import dcode.domain.entity.PromotionProducts;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PromotionRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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

    public List<Promotion> getPromotionByProductId(int productId) {
        String query = "SELECT pm.* FROM `promotion` pm INNER JOIN `promotion_products` pp " +
                "WHERE pp.product_id = :productId ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId);

        return namedParameterJdbcTemplate.query(
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

    public List<PromotionProducts> getPromotionProducts(int promotionId) {
        String query = "SELECT * FROM `promotion_products` WHERE promotion_id = :promotionId";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("promotionId", promotionId);

        return namedParameterJdbcTemplate.query(
                query,
                params,
                (rs, rowNum) -> PromotionProducts.builder()
                        .id(rs.getInt("id"))
                        .promotionId(rs.getInt("promotion_id"))
                        .productId(rs.getInt("product_id"))
                        .build()
        );
    }
}
