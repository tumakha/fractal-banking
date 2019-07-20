package fractal.banking.dto;

import fractal.banking.domain.Transaction;
import fractal.banking.domain.TransactionCategory;
import fractal.banking.domain.TransactionId;
import lombok.Data;

import static org.springframework.beans.BeanUtils.copyProperties;


/**
 * @author Yuriy Tumakha
 */
@Data
public class TransactionDTO {

  private Long bankId;

  private String transactionId;

  private TransactionCategoryDTO transactionCategory;

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

  public static TransactionDTO of(Transaction t) {
    TransactionDTO dto = new TransactionDTO();
    copyProperties(t, dto);

    TransactionId tid = t.getTransactionId();
    dto.setBankId(tid.getBankId());
    dto.setTransactionId(tid.getTransactionId());

    TransactionCategory transCategory = t.getTransactionCategory();
    if (transCategory != null) {
      TransactionCategoryDTO transCatDTO = TransactionCategoryDTO.of(
          CategoryDTO.of(transCategory.getCategory()), transCategory.isUserDefined());

      dto.setTransactionCategory(transCatDTO);
    }

    return dto;
  }

}
