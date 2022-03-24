package dcode.model.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
public class ProductInfoRequest {

    @Positive
    private int productId;

    @NotNull
    private int[] promotionIds;
}
