package dcode.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {
    @Id
    private int id;
    private String promotion_type; //쿠폰 타입 (쿠폰, 코드)
    private String name;
    private String discount_type; // WON : 금액 할인, PERCENT : %할인
    private int discount_value; // 할인 금액 or 할인 %
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate use_started_at; // 쿠폰 사용가능 시작 기간
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate use_ended_at; // 쿠폰 사용가능 종료 기간

    public boolean isUsableCoupon(LocalDate useDate) {
       return useDate.compareTo(use_started_at) >= 0 && useDate.compareTo(use_ended_at) <= 0;
    }
}
