package dcode.service;

import dcode.exception.NoSuchProductException;
import dcode.model.request.ProductInfoRequest;
import dcode.repository.ProductRepository;
import dcode.repository.ProductRepositoryImpl;
import dcode.repository.PromotionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @InjectMocks
  ProductService productService;

  @Mock
  ProductRepository productRepository;

  @Mock
  PromotionRepository promotionRepository;

  @Test
  @DisplayName("프로모션 적용된 상품가격 조회 : 존재하지 않는(DB에 없는) 프로덕트일 경우 NoSuchProductException 발생")
  void getProductAmount() {
    // given
    var requestBody = ProductInfoRequest.builder().productId(1).couponIds(new int[]{1, 2}).build();
    // when
    when(productRepository.getProduct(requestBody.getProductId())).thenReturn(Optional.empty());
    when(promotionRepository.getPromotionsById(any())).thenReturn(List.of());

    // then
    assertThrows(NoSuchProductException.class, () -> productService.getProductAmount(requestBody));

    verify(productRepository, times(1)).getProduct(requestBody.getProductId());
    verifyNoInteractions(promotionRepository);
  }

}