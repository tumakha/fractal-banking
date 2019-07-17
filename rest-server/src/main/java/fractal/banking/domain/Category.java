package fractal.banking.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Yuriy Tumakha
 */
@Entity
@Table(name = "category")
@Data
public class Category {

  @Id
  private String name;

  private boolean userDefined;

}
