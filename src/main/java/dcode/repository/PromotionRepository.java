package dcode.repository;


import dcode.domain.entity.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class PromotionRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public int checkPromotion(int productId, int promotionId) {
        String query="select(exists(select * from PROMOTION_PRODUCTS where PRODUCT_ID = ? and PROMOTION_ID = ?))";
        return jdbcTemplate.queryForObject(query, int.class, productId, promotionId);
    }

    public List<Promotion> getPromotion(int[] ids) {
        List<int[]> ids1 = List.of(ids);
        SqlParameterSource params = new MapSqlParameterSource("ids", Arrays.stream(ids).boxed().collect(Collectors.toList()));
        String query = "SELECT * FROM PROMOTION WHERE id in (:ids)";


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
}
