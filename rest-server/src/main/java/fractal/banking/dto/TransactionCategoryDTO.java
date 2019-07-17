package fractal.banking.dto;

import fractal.banking.domain.TransactionCategory;
import fractal.banking.domain.TransactionId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yuriy Tumakha
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCategoryDTO {

  private Long bankId;

  private String transactionId;

  private CategoryDTO category;

  private boolean userDefined;

  public static TransactionCategoryDTO of(TransactionCategory tc) {
    TransactionId tid = tc.getTransactionId();
    return new TransactionCategoryDTO(tid.getBankId(), tid.getTransactionId(),
        CategoryDTO.of(tc.getCategory()), tc.isUserDefined());
  }

}
