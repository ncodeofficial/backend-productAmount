package dcode.repository;

import dcode.domain.entity.Promotion;

import java.util.List;

public interface PromotionRepository {

  List<Promotion> getPromotionsById(List<Integer> idList);
}
