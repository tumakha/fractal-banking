package fractal.banking;

import fractal.sdk.FractalSDK;
import fractal.sdk.banking.model.AccountResponse;
import fractal.sdk.banking.model.Transaction;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static java.lang.System.out;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore("Excluded from Gradle build (only for manual run in IDE)")
public class FractalSDKIntegrationTest {

  private static final long BANK_ID = 6L;
  private static final long COMPANY_ID = 6L;
  private static final String ACCOUNT_ID = "fakeacc";

  @Autowired
  private FractalSDK fractalSDK;

  @Test
  public void testFractalSDK() throws IOException {
    fractalSDK.setDebug(true);
    fractalSDK.getAuthHeader();

    List<Transaction> transactionListByComp = fractalSDK.getTransactions(BANK_ID, ACCOUNT_ID, COMPANY_ID).getResults();

    assertThat(transactionListByComp, hasSize(greaterThan(15)));
    out.println(transactionListByComp);

    List<Transaction> transactionList = fractalSDK.getTransactions(BANK_ID, ACCOUNT_ID, null).getResults();

    assertThat(transactionList, hasSize(greaterThan(30)));
    out.println(transactionList);

    AccountResponse bankAccount = fractalSDK.getBankAccount(BANK_ID, ACCOUNT_ID);

    assertThat(bankAccount.getAccountId(), equalTo(ACCOUNT_ID));
    out.println(bankAccount);
  }

}
