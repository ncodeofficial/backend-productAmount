package dcode.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PromotionProductsRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Integer> getPromotionProducts(int product_id) {
        String query = "SELECT promotion_id FROM `promotion_products` WHERE product_id = :product_id ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("product_id", product_id);

        return namedParameterJdbcTemplate.queryForList(
                query,
                params,
                Integer.class
        );
    }
}
