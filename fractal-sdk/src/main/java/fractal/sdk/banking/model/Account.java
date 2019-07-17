package fractal.sdk.banking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Yuriy Tumakha
 */
@Data
public class Account {

  @JsonProperty("SecondaryIdentification")
  private String secondaryIdentification;

  @JsonProperty("SchemeName")
  private String schemeName;

  @JsonProperty("Identification")
  private String identification;

  @JsonProperty("Name")
  private String name;

}
