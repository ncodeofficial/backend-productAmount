package dcode.model.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Builder
public class ProductInfoRequest {
    @NotBlank
    private int productId;
    private int[] couponIds;
}
