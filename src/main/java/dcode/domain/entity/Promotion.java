package dcode.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "promotion")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Promotion {

	@Id
	private Integer id;

	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "promotion_type")
	private PromotionType promotionType; //쿠폰 타입 (쿠폰, 코드)

	@Enumerated(EnumType.STRING)
	@Column(name = "discount_type")
	private DiscountType discountType; // WON : 금액 할인, PERCENT : %할인

	@Column(name = "discount_value")
	private int discountValue; // 할인 금액 or 할인 %

	@Column(name = "use_started_at")
	private Date useStartedAt; // 쿠폰 사용가능 시작 기간

	@Column(name = "use_ended_at")
	private Date useEndedAt; // 쿠폰 사용가능 종료 기간

	@Builder
	public Promotion(PromotionType promotionType, DiscountType discountType, String name, int discountValue,
		Date useStartedAt, Date useEndedAt) {
		this.promotionType = promotionType;
		this.discountType = discountType;
		this.name = name;
		this.discountValue = discountValue;
		this.useStartedAt = useStartedAt;
		this.useEndedAt = useEndedAt;
	}
}
