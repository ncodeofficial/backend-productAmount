package dcode.model.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInfoRequest {

    private int productId;
    private List<Integer> couponIds;
}
