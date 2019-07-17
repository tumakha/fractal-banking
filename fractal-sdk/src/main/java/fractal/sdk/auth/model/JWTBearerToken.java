package fractal.sdk.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * JWT Bearer Token.
 *
 * @author Yuriy Tumakha
 */
@Data
public class JWTBearerToken {

  @JsonProperty("access_token")
  private String accessToken;

  private String expires;

  private String partner;

  @JsonProperty("partner_name")
  private String partnerName;

  private String permissions;

  @JsonProperty("token_type")
  private String tokenType;

  public String getAuthHeader() {
    return "Bearer " + accessToken;
  }

}
