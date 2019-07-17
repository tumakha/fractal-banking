package fractal.sdk.banking.model;

import lombok.Data;

import java.util.List;

/**
 * @author Yuriy Tumakha
 */
@Data
public class TransactionsResponse {

  private Integer pageNo;

  private List<Transaction> results;

  private Integer noPages;

}
