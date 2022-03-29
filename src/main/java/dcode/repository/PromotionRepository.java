package dcode.repository;

import dcode.domain.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    // 쿠폰은 요청객체로 넘어옴
    // 넘어온 쿠폰에 대해 사용 가능한지 유효성 체크

//    select pr.id, pr.name, pr.price, case (when (pm.promotion_type = 'coupon' and discount_type = 'won' and pr.price / discunt_value < 10000) or (pm.promotion_type = 'coupon' and pm.discount_type = 'percent' and pr.price * (100 - pm.discount_value) / 100 < 10000) then 0 else 1 end;) as is_payable from production pr // JOIN문

    // 쿠폰 번호를 통해 사용 가능한 쿠폰인지 체크해서 적용가능한지 검증 로직 작성
//    String query = "select\n" +
//            "    pr.id, pr.name, pr.price,pm.id, pm.name, pm.promotion_type, pm.discount_type, pm.use_ended_at, pm.use_started_at, pm.discount_value\n" +
//            "from\n" +
//            "    product pr\n" +
//            "  inner join\n" +
//            "      promotion_products pp\n" +
//            "  on\n" +
//            "      pr.id = pp.product_id\n" +
//            "  inner join\n" +
//            "      promotion pm\n" +
//            "  on\n" +
//            "      pp.promotion_id = pm.id\n" +
//            "where\n" +
//            "    pr.id = ?1 and pm.id in ?2 and\n" +
//            "    TIMESTAMPADD(MONTH, -1, now()) > pm.use_started_at and TIMESTAMPADD(MONTH, -1, now()) < pm.use_ended_at";
//    // JPQL 로직을 처리하지 말라
//    // JPQL은 데이터 가져오기만 하고
//    // 처리는 어플리케이션 단에서 처리
//    @Query(value = query, nativeQuery = true)
//    Optional<List<Promotion>> selectAllSQL(int product_id, int[] coupons);

}
