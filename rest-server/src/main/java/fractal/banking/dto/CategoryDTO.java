package fractal.banking.dto;

import fractal.banking.domain.Category;
import lombok.Value;

/**
 * @author Yuriy Tumakha
 */
@Value
public class CategoryDTO {

  private String name;

  private boolean userDefined;

  public static CategoryDTO of(Category cat) {
    return new CategoryDTO(cat.getName(), cat.isUserDefined());
  }

}
