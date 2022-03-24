package dcode.repository;

import dcode.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.function.BiFunction;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final RowMapper<Product> productMapper = (rs, rowNum) -> Product.builder()
      .id(rs.getInt("id"))
      .name(rs.getString("name"))
      .price(rs.getInt("price"))
      .build();

  // TODO : service 레벨에서 핸들링 vs gloal 하게 핸들링하게
  //  IncorrectResultSizeDataAccessException - if the query does not return exactly one row
  // DataAccessException - if the query fails
    public Optional<Product> getProduct(int id) {
        String query = "SELECT * FROM `product` WHERE id = :id ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

      return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(
        query,
        params,
        productMapper
      ));
    }


}

