package fractal.banking.config;

import fractal.banking.categorization.CategorizationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yuriy Tumakha
 */
@Configuration
public class CategorizationConfig {

  private static final String UNCATEGORISED = "Uncategorised";

  @Bean
  public CategorizationService сategorizationService() {
    return new CategorizationService(UNCATEGORISED);
  }

}
