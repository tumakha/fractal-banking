package fractal.banking.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Yuriy Tumakha
 */
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionId implements Serializable {

  private Long bankId;

  private String transactionId;

}
