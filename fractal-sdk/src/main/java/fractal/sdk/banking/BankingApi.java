package fractal.sdk.banking;

import fractal.sdk.banking.model.AccountResponse;
import fractal.sdk.banking.model.TransactionsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Yuriy Tumakha
 */
public interface BankingApi {

  /**
   * Retrieve Account.
   *
   * @param bankId        Bank ID (required)
   * @param accountId     Account ID (required)
   * @return Call&lt;AccountResponse&gt;
   */
  @GET("v1/banking/{bankId}/accounts/{accountId}")
  Call<AccountResponse> bankAccountGet(
      @Path("bankId") Long bankId,
      @Path("accountId") String accountId
  );

  /**
   * Retrieve Account Transactions.
   *
   * @param bankId        Bank ID (required)
   * @param accountId     Account ID (required)
   * @param companyId     Company ID (optional)
   * @return Call&lt;TransactionsResponse&gt;
   */
  @GET("v1/banking/{bankId}/accounts/{accountId}/transactions")
  Call<TransactionsResponse> transactionsByBankAndAccountGet(
      @Path("bankId") Long bankId,
      @Path("accountId") String accountId,
      @Query("companyId") Long companyId
  );

}
