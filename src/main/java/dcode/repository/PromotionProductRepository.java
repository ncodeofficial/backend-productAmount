package dcode.repository;

import dcode.domain.entity.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PromotionProductRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public List<Integer> getPromotionIdList(Integer productId) {
        String query = "SELECT PROMOTION_ID FROM PROMOTION_PRODUCTS WHERE product_id = :productId";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId);
//        Map params = Collections.singletonMap("ids", ids);
        return namedParameterJdbcTemplate.query(
                query,
                params,
                (rs, rowNum) -> rs.getInt("PROMOTION_ID")
                        /*Promotion.builder()
                        .id(rs.getInt("ID"))
                        .promotion_type(rs.getString("PROMOTION_TYPE"))
                        .name(rs.getString("NAME"))
                        .discount_type(rs.getString("DISCOUNT_TYPE"))
                        .discount_value(rs.getInt("DISCOUNT_VALUE"))
                        .use_started_at(rs.getDate("USE_STARTED_AT"))
                        .use_ended_at(rs.getDate("USE_ENDED_AT"))
                        .build()*/
        );
    }
}
