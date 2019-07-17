package fractal.banking.dto;

import fractal.banking.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yuriy Tumakha
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

  private String name;

  private boolean userDefined;

  public static CategoryDTO of(Category cat) {
    return new CategoryDTO(cat.getName(), cat.isUserDefined());
  }

}
