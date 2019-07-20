package fractal.banking.dto;

import fractal.banking.domain.TransactionCategory;
import fractal.banking.domain.TransactionId;
import lombok.Value;

/**
 * @author Yuriy Tumakha
 */
@Value
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

  static TransactionCategoryDTO of(CategoryDTO category, boolean userDefined) {
    return new TransactionCategoryDTO(null, null, category, userDefined);
  }

}
