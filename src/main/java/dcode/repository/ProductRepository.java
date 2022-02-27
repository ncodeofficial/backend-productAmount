package dcode.repository;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Product getProduct(int id) {
        String query = "SELECT * FROM product WHERE id = :id ";
        
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
    
    public Promotion getPromotion(int[] promotion_id) {
    	String promotion_query = "SELECT pro.id,pro.promotion_type, pro.name, pro.discount_type, "
        		+ "pro.discount_value, pro.use_started_at, pro.use_ended_at "
        		+ "FROM promotion pro inner join promotion_products using (id) "
        		+ "WHERE pro.id = :id ";

    	int promotion_number = (int)(Math.random() * promotion_id.length) + 1;
    	
    	MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", promotion_number);
        
        return  namedParameterJdbcTemplate.queryForObject(
        		promotion_query,
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
