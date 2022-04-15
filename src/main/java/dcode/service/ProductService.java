package dcode.service;

import static dcode.service.ErrorMessages.*;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import dcode.domain.entity.Product;
import dcode.domain.entity.Promotion;
import dcode.domain.entity.PromotionType;
import dcode.model.request.ProductInfoRequest;
import dcode.model.response.ProductAmountResponse;
import dcode.repository.ProductJpaRepository;
import dcode.repository.PromotionJpaRepository;
import dcode.repository.PromotionProductsJpaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {

	private final int MINIMUM_PRODUCT_PRICE = 10000; // 할인이 적용된 최소 상품 금액
	private final int CUTTING_PRICE = 1000;			 // 최종 상품 금액 절삭 단위

	private final ProductJpaRepository productRepository;
	private final PromotionJpaRepository promotionRepository;
	private final PromotionProductsJpaRepository promotionProductsRepository;

	public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
		final Product product = productRepository.findById(request.getProductId())
			.orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND.getMessage()));

		final int originPrice = product.getPrice();
		final int discountPrice = discount(request.getCouponIds(), product);

		return ProductAmountResponse.builder()
			.finalPrice(calculateFinalPrice(originPrice, discountPrice))
			.originPrice(originPrice)
			.discountPrice(discountPrice)
			.name(product.getName())
			.build();
	}

	private int discount(List<Integer> couponIds, Product product) {
		final int originPrice = product.getPrice();
		int discountPrice = 0;

		for (Integer couponId : couponIds) {
			final Promotion promotion = promotionRepository.findById(couponId)
				.orElseThrow(() -> new EntityNotFoundException(PROMOTION_NOT_FOUND.getMessage()));
			promotionProductsRepository.findByProductAndPromotion(product, promotion)
				.orElseThrow(() -> new EntityNotFoundException(PROMOTION_PRODUCT_NOT_FOUND.getMessage()));

			final PromotionType promotionType = promotion.getPromotionType();
			final int discountValue = promotion.getDiscountValue();

			if (promotionType.equals(PromotionType.COUPON)) {
				discountPrice += discountValue;
			} else if (promotionType.equals(PromotionType.CODE)) {
				discountPrice += originPrice * discountValue / 100;
			}
		}

		return discountPrice;
	}

	private int calculateFinalPrice(int originPrice, int discountPrice) {
		int finalPrice = originPrice - discountPrice;

		finalPrice = finalPrice / CUTTING_PRICE * CUTTING_PRICE;
		finalPrice = Math.max(finalPrice, MINIMUM_PRODUCT_PRICE);

		return finalPrice;
	}
}
