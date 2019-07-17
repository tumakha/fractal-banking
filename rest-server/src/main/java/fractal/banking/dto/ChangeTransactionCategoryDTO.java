package fractal.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static fractal.banking.dto.CreateCategoryDTO.CATEGORY_MAX_LENGTH;

/**
 * @author Yuriy Tumakha
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeTransactionCategoryDTO {

  @NotBlank
  @Size(max = CATEGORY_MAX_LENGTH)
  private String category;

}
