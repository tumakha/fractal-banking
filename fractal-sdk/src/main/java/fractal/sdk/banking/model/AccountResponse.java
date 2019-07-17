package fractal.sdk.banking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Yuriy Tumakha
 */
@Data
public class AccountResponse {

  @JsonProperty("Account")
  private Account account;

  @JsonProperty("AccountId")
  private String accountId;

  private Long companyId;

  private Long bankId;

  private boolean revoked;

  private String dateAuthorised;

}
