package dcode.repository;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class PromotionRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public List<Promotion> getPromotionList(List<Integer> ids) {
        String query = "SELECT * FROM promotion WHERE id IN (:ids)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);
//        Map params = Collections.singletonMap("ids", ids);
        return namedParameterJdbcTemplate.query(
                query,
                params,
                (rs, rowNum) -> Promotion.builder()
                        .id(rs.getInt("ID"))
                        .promotion_type(rs.getString("PROMOTION_TYPE"))
                        .name(rs.getString("NAME"))
                        .discount_type(rs.getString("DISCOUNT_TYPE"))
                        .discount_value(rs.getInt("DISCOUNT_VALUE"))
                        .use_started_at(rs.getDate("USE_STARTED_AT"))
                        .use_ended_at(rs.getDate("USE_ENDED_AT"))
                        .build()
        );
    }
}
