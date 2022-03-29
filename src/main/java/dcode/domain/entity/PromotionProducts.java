package dcode.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class PromotionProducts {
    @Id
    private int id;
    private int promotionId;
    private int productId;
}
