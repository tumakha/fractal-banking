package fractal.banking.config;

import fractal.sdk.FractalSDK;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URL;

/**
 * @author Yuriy Tumakha
 */
@Configuration
@ConfigurationProperties("fractal")
@Data
public class FractalSdkConfig {

  private String authBase;
  private String bankingBase;
  private String apiKey;
  private String partnerId;

  @Bean
  public FractalSDK fractalSDK() throws IOException {
    FractalSDK fractalSDK = new FractalSDK(new URL(authBase), new URL(bankingBase), apiKey, partnerId);
    Logger logger = LoggerFactory.getLogger(fractalSDK.getClass());

    fractalSDK.setDebug(logger.isDebugEnabled());
    if (!logger.isDebugEnabled()) fractalSDK.getAuthHeader();
    return fractalSDK;
  }

}
