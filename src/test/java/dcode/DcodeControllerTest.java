package dcode;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import dcode.controller.DcodeController;
import dcode.model.request.ProductInfoRequest;
import dcode.service.ProductService;

@WebMvcTest(DcodeController.class)
public class DcodeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	@Test
	public void productMvcTest() throws Exception {
		
		int[] couponIds = {3,4}; // 존재하지 않는 쿠폰 아이디 입력 테스트
		
		ProductInfoRequest request = ProductInfoRequest.builder()
						                .productId(1)
						                .couponIds(couponIds)
						                .build();
		
		mockMvc.perform(
			MockMvcRequestBuilders
			.get("/dcode/product-amount")
			.requestAttr("ProductInfoRequest", request))
		.andExpect(status().isOk())
		.andDo(print());
	}
}
