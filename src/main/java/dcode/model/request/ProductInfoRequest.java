package dcode.model.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Builder
public class ProductInfoRequest {
    @NotNull(message="product id가 없습니다..")
    @Min(value=1)
    private int productId;
    private int[] couponIds;
}
