package dcode.repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import lombok.RequiredArgsConstructor;

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
        } catch(Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
    
    public List<Promotion> getPromotion(int[] ids) {
    	
    	List<Integer> couponList = Arrays.stream(ids)
    									.boxed()
    									.collect(Collectors.toList());
    	
    	String query = "SELECT * "
    					+ "FROM `promotion` "
    					+ "WHERE id in (:ids) "
    						+ "AND now() >= use_started_at "
    						+ "AND use_ended_at >= now() ";
    	
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("ids", couponList);
    	
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
