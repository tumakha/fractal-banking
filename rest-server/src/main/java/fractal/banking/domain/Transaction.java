package fractal.banking.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Yuriy Tumakha
 */
@Entity
@Table(name = "transaction")
@Data
public class Transaction {

  @EmbeddedId
  private TransactionId transactionId;

  @OneToOne
  @JoinColumns({
      @JoinColumn(name = "bankId"),
      @JoinColumn(name = "transactionId")
  })
  private TransactionCategory transactionCategory;

  private String accountId;

  private Long companyId;

  private String merchantName;

  private String description;

  private String amount;

  private String currencyCode;

  private String type;

  private String bookingDate;

  private String status;

  private String reference;

  private String transactionCode;

  private String transactionSubCode;

  private String proprietaryCode;

  private String proprietarySubCode;

}
