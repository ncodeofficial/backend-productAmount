package dcode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromotionProperties {

  public static final int MIN_FINAL_DISCOUNTED_PRICE = 10_000;
}
