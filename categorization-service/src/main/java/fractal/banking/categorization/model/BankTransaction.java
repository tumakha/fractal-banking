package fractal.banking.categorization.model;

import lombok.Data;

/**
 * @author Yuriy Tumakha
 */
@Data
public class BankTransaction {

  private String userId;

  private String merchant;

  private String description;

  private String amount;

  private String currencyCode;

}
