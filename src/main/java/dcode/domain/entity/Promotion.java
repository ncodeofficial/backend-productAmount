package dcode.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class Promotion {
    @Id
    private int id;
    private String promotion_type; //쿠폰 타입 (쿠폰, 코드)
    private String name;
    private String discount_type; // WON : 금액 할인, PERCENT : %할인
    private int discount_value; // 할인 금액 or 할인 %
    private Date use_started_at; // 쿠폰 사용가능 시작 기간
    private Date use_ended_at; // 쿠폰 사용가능 종료 기간

    // 적용가능한 쿠폰인지 검증
    public boolean isUsable(Date date) {
        // 들어온 날짜와 비교
        if(date.compareTo(getUse_ended_at()) <= 0 && date.compareTo(getUse_started_at()) >= 0) return true;
        return false;
    }
}
