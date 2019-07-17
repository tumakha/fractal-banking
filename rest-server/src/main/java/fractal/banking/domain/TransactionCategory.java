package fractal.banking.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Yuriy Tumakha
 */
@Entity
@Table(name = "transaction_category_map")
@Data
public class TransactionCategory {

  @Id
  private TransactionId transactionId;

  @ManyToOne
  @JoinColumn(name = "category_name")
  private Category category;

  private boolean userDefined;

}
