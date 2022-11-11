package dcode.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfoRequest {
    @NotNull(message="product id가 없습니다.")
    @Min(value=1)
    private int productId;
    private int[] couponIds;
}
