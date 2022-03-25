package dcode.repository;

import dcode.domain.entity.Promotion;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface PromotionRepository {

  List<Promotion> getPromotionsByProductIdAndPromotionId(int productId, int[] idList);
}
