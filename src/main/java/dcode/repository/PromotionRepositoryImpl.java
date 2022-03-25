package dcode.repository;

import dcode.domain.entity.DiscountType;
import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.management.StringValueExp;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class PromotionRepositoryImpl implements PromotionRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private static final RowMapper<Promotion> promotionMapper = (rs, rowNum) -> Promotion.builder()
    .id(rs.getInt("id"))
    .promotion_type(rs.getString("promotion_type"))
    .name(rs.getString("name"))
    .discount_type(DiscountType.valueOf(rs.getString("discount_type")))
    .discount_value(rs.getInt("discount_value"))
    .use_started_at(rs.getDate("use_started_at"))
    .use_ended_at(rs.getDate("use_ended_at"))
    .build();

  @Override
  public List<Promotion> getPromotionsByProductIdAndPromotionId(int productId, int[] idList) {
    return Arrays.stream(idList).mapToObj(id -> getPromotionByIds(productId, id)).collect(Collectors.toList());
  }


  private Promotion getPromotionByIds(int productId, int promotionId) {
    String query = "SELECT * FROM `promotion` p \n" +
      "INNER JOIN ( \n" +
      "SELECT promotion_id FROM `promotion_products` WHERE product_id = :productId AND promotion_id = :promotionId" +
      ") B \n" +
      "ON p.id = B.promotion_id";

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("productId", productId);
    params.addValue("promotionId", promotionId);
    return namedParameterJdbcTemplate.queryForObject(
      query,
      params,
      promotionMapper);
  }
}

