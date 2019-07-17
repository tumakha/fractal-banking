package fractal.banking;

import fractal.sdk.FractalSDK;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static java.lang.String.format;
import static java.net.HttpURLConnection.*;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author Yuriy Tumakha
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWireMock(port = 8013)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class WebAppStubTest extends StubBase {

  private static final int BANK_ID = 6;
  private static final String ACCOUNT_ID = "fakeacc";
  private static final String TRANSACTION_ID = "25a4d256-094f-4637-bea0-5f82760674b0";
  private static final String TOKEN_URL = "/v1/token";
  private static final String ACCOUNT_URL = format("/v1/banking/%d/accounts/%s", BANK_ID, ACCOUNT_ID);
  private static final String TRANSACTIONS_URL = format("/v1/banking/%d/accounts/%s/transactions", BANK_ID, ACCOUNT_ID);

  @LocalServerPort
  private int serverPort;

  @Autowired
  private FractalSDK fractalSDK;

  @Value("classpath:fractal-stubs/token-response.json")
  private Resource tokenResponse;

  @Value("classpath:fractal-stubs/account-response.json")
  private Resource accountResponse;

  @Value("classpath:fractal-stubs/transactions-response.json")
  private Resource transactionsResponse;

  @Value("classpath:transactions-all.json")
  private Resource transactionsAll;

  @Value("classpath:transactions-coffee.json")
  private Resource transactionsCoffee;

  @Value("classpath:transactions-online-shopping.json")
  private Resource transactionsOnlineShopping;

  @Value("classpath:create-category.json")
  private Resource createCategory;

  @Value("classpath:create-category-response.json")
  private Resource createCategoryResponse;

  @Value("classpath:change-category.json")
  private Resource changeTransactionCategory;

  @Value("classpath:change-category-response.json")
  private Resource changeTransactionCategoryResponse;

  @Value("classpath:categories-all.json")
  private Resource categoriesAll;

  @Before
  public void setUp() throws Exception {
    RestAssured.port = serverPort;
    expectPostCall(TOKEN_URL, tokenResponse);
    expectGetCall(ACCOUNT_URL, accountResponse);
    expectGetCall(TRANSACTIONS_URL, transactionsResponse);
    fractalSDK.getAuthHeader();
  }

  @Test
  public void testSyncTransactions() {
    when().
        put(format("/v1/bank/%d/account/%s/transaction/sync", BANK_ID, ACCOUNT_ID)).
        then().
        statusCode(HTTP_ACCEPTED).
        contentType(JSON).
        body("message", equalTo("Account transactions synchronization started"),
            "accountId", equalTo(ACCOUNT_ID));
  }

  @Test
  public void testGetAllGetByCategory() throws IOException {
    when().
        put(format("/v1/bank/%d/account/%s/transaction/sync", BANK_ID, ACCOUNT_ID)).
        then().
        statusCode(HTTP_ACCEPTED).
        contentType(JSON).
        body("message", equalTo("Account transactions synchronization started"));

    // Get all transactions
    String response = when().
        get(format("/v1/bank/%d/account/%s/transaction", BANK_ID, ACCOUNT_ID)).
        then().
        statusCode(HTTP_OK).
        contentType(JSON).
        extract().body().asString();

    assertThatJson(response).when(IGNORING_ARRAY_ORDER).isEqualTo(getResourceAsString(transactionsAll));

    // Get transactions by category "Coffee"
    String responseCategoryCoffe = when().
        get(format("/v1/bank/%d/account/%s/transaction?category=Coffee", BANK_ID, ACCOUNT_ID)).
        then().
        statusCode(HTTP_OK).
        contentType(JSON).
        extract().body().asString();

    assertThatJson(responseCategoryCoffe).when(IGNORING_ARRAY_ORDER).isEqualTo(getResourceAsString(transactionsCoffee));
  }

  @Test
  public void testCreateCategoryGetAllAndChangeTransactionCategory() throws IOException {
    when().
        put(format("/v1/bank/%d/account/%s/transaction/sync", BANK_ID, ACCOUNT_ID)).
        then().
        statusCode(HTTP_ACCEPTED).
        body("message", equalTo("Account transactions synchronization started"));

    // Create category "Online Shopping"
    String response = given().
        contentType(JSON).
        body(getResourceAsString(createCategory)).
        post("/v1/category").
        then().
        contentType(JSON).
        statusCode(HTTP_CREATED).
        extract().body().asString();

    assertThatJson(response).isEqualTo(getResourceAsString(createCategoryResponse));

    // Get all categories
    String categoriesResponse = when().
        get("/v1/category").
        then().
        statusCode(HTTP_OK).
        contentType(JSON).
        extract().body().asString();

    assertThatJson(categoriesResponse).when(IGNORING_ARRAY_ORDER).isEqualTo(getResourceAsString(categoriesAll));

    // Change transaction category
    String response2 = given().
        contentType(JSON).
        body(getResourceAsString(changeTransactionCategory)).
        patch(format("/v1/bank/%d/transaction/%s/category", BANK_ID, TRANSACTION_ID)).
        then().
        statusCode(HTTP_OK).
        contentType(JSON).
        extract().body().asString();

    assertThatJson(response2).isEqualTo(getResourceAsString(changeTransactionCategoryResponse));

    // Get transactions with new category "Online Shopping"
    String transactionsResponse = when().
        get(format("/v1/bank/%d/account/%s/transaction?category=Online Shopping", BANK_ID, ACCOUNT_ID)).
        then().
        statusCode(HTTP_OK).
        contentType(JSON).
        extract().body().asString();

    assertThatJson(transactionsResponse).isEqualTo(getResourceAsString(transactionsOnlineShopping));
  }

  @Test
  public void testCreateCategoryNameAlreadyExists() {
    given().
        contentType(JSON).
        accept(JSON).
        body("{\"name\": \"Travel\"}").
        post("/v1/category").
        then().
        statusCode(HTTP_CONFLICT);
  }

}
