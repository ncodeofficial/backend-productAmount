package dcode.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "PROMOTION_TYPE")
    private String promotion_type; //쿠폰 타입 (쿠폰, 코드)
    @Column(name = "NAME")
    private String name;
    @Column(name = "DISCOUNT_TYPE")
    private String discount_type; // WON : 금액 할인, PERCENT : %할인
    @Column(name = "DISCOUNT_VALUE")
    private int discount_value; // 할인 금액 or 할인 %
    @Column(name = "USE_STARTED_AT")
    private Date use_started_at; // 쿠폰 사용가능 시작 기간
    @Column(name = "USE_ENDED_AT")
    private Date use_ended_at; // 쿠폰 사용가능 종료 기간
}
