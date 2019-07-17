package fractal.sdk.banking.model;

import lombok.Data;

/**
 * @author Yuriy Tumakha
 */
@Data
public class Transaction {

  private String amount;

  private String accountId;

  private String description;

  private Merchant merchant;

  private String transactionSubCode;

  private String transactionCode;

  private String type;

  private String transactionId;

  private String reference;

  private Long companyId;

  private Long bankId;

  private String proprietarySubCode;

  private String bookingDate;

  private String proprietaryCode;

  private String currencyCode;

  private String status;

}
