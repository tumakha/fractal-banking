package fractal.banking.categorization.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Yuriy Tumakha
 */
@Data
@AllArgsConstructor
public class CategoryRelevance {

  private String category;
  private long relevance;

}
