package fractal.banking.dto;

import fractal.sdk.banking.model.Account;
import fractal.sdk.banking.model.AccountResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yuriy Tumakha
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsSyncStartedDTO {

  private String message;

  private Long bankId;

  private Long companyId;

  private String accountId;

  private Account account;

  public static TransactionsSyncStartedDTO of(String message, AccountResponse acc) {
    return new TransactionsSyncStartedDTO(message, acc.getBankId(), acc.getCompanyId(),
        acc.getAccountId(), acc.getAccount());
  }

}
