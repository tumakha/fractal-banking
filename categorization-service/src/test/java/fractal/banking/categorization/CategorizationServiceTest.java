package fractal.banking.categorization;

import fractal.banking.categorization.model.BankTransaction;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

/**
 * @author Yuriy Tumakha
 */
public class CategorizationServiceTest {

  private static final String UNCATEGORISED = "Uncategorised";
  private static final String TRAVEL = "Travel";
  private static final String COFFEE = "Coffee";
  private static final String EATING_OUT = "Eating Out";
  private static final String SHOPPING = "Shopping";
  private static final String USER_1 = "user1";
  private static final String USER_2 = "user2";

  @Test
  public void testSystemDefinedCategories() {
    List<String> categories = newCategorizationService().getSystemDefinedCategories();
    assertThat(categories, hasItems(COFFEE, TRAVEL, EATING_OUT, SHOPPING));
  }

  @Test
  public void testGetCategory() {
    CategorizationService categorizationService = newCategorizationService();

    String travelCategory = categorizationService.getCategory(getBankTransaction(USER_1,
        "CABS", "LONDON TAXI JOURNE LONDON  E1"));
    assertThat(travelCategory, equalTo(TRAVEL));

    String coffeeCategory = categorizationService.getCategory(getBankTransaction(USER_1,
        "Starbucks", "STARBUCKS VICTORIA STN"));
    assertThat(coffeeCategory, equalTo(COFFEE));
  }

  @Test
  public void testImproveModelWorksPerUser() {
    CategorizationService categorizationService = newCategorizationService();
    String merchant = "AMAZON UK RETAIL";
    String description = "INT'L 0020481195 AMAZON UK RETAIL A AMAZON.CO.UK";

    String categoryForUser1 = categorizationService.getCategory(getBankTransaction(USER_1, merchant, description));
    String categoryForUser2 = categorizationService.getCategory(getBankTransaction(USER_2, merchant, description));
    assertThat(categoryForUser1, equalTo(SHOPPING));
    assertThat(categoryForUser2, equalTo(SHOPPING));

    // improve model by request from user
    categorizationService.improveModel(SHOPPING, "Books", getBankTransaction(USER_1, merchant, description));

    String newCategoryForUser1 = categorizationService.getCategory(getBankTransaction(USER_1, merchant, description));
    String newCategoryForUser2 = categorizationService.getCategory(getBankTransaction(USER_2, merchant, description));
    assertThat(newCategoryForUser1, equalTo("Books"));
    assertThat(newCategoryForUser2, equalTo(SHOPPING));
  }

  private CategorizationService newCategorizationService() {
    return new CategorizationService(UNCATEGORISED);
  }

  private BankTransaction getBankTransaction(String userId, String merchant, String description) {
    BankTransaction transaction = new BankTransaction();
    transaction.setUserId(userId);
    transaction.setMerchant(merchant);
    transaction.setDescription(description);
    return transaction;
  }

}
