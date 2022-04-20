package dcode.repository;

import dcode.domain.entity.Product;
import dcode.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import static dcode.exception.ErrorCode.INVALID_PRODUCT;

@RequiredArgsConstructor
@Repository
public class ProductRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Product getProduct(int id) {
        String query = "SELECT * FROM `product` WHERE id = :id ";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        try {
            return namedParameterJdbcTemplate.queryForObject(
                    query,
                    params,
                    (rs, rowNum) -> Product.builder()
                            .id(rs.getInt("id"))
                            .name(rs.getString("name"))
                            .price(rs.getInt("price"))
                            .build()
            );
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException(INVALID_PRODUCT);
        }
    }
}
