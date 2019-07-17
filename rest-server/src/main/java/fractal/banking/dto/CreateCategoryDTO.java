package fractal.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Yuriy Tumakha
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryDTO {

  public static final int CATEGORY_MAX_LENGTH = 30;

  @NotBlank
  @Size(max = CATEGORY_MAX_LENGTH)
  private String name;

}
