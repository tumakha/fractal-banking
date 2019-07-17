package fractal.sdk;

import fractal.sdk.auth.AuthApi;
import fractal.sdk.auth.model.JWTBearerToken;
import fractal.sdk.banking.BankingApi;
import fractal.sdk.banking.model.AccountResponse;
import fractal.sdk.banking.model.TransactionsResponse;
import fractal.sdk.client.RestClientBase;
import okhttp3.Headers;

import java.io.IOException;
import java.net.URL;

public class FractalSDK extends RestClientBase {

  private static final String X_API_KEY = "x-api-key";
  private static final String X_PARTNER_ID = "x-partner-id";

  private AuthApi authApi;
  private BankingApi bankingApi;

  public FractalSDK(URL authBaseUrl, URL bankingBaseApi, String apiKey, String partnerId) {
    super(globalHeaders(apiKey, partnerId));
    authApi = createService(authBaseUrl, AuthApi.class);
    bankingApi = createService(bankingBaseApi, BankingApi.class);
  }

  private static Headers globalHeaders(String apiKey, String partnerId) {
    return new Headers.Builder()
        .add(ACCEPT, APPLICATION_JSON)
        .add(CONTENT_TYPE, APPLICATION_JSON)
        .add(X_API_KEY, apiKey)
        .add(X_PARTNER_ID, partnerId)
        .build();
  }

  @Override
  public String getAuthHeader() throws IOException {
    String authHeader = getToken().getAuthHeader();
    chainInterceptor.setAuthHeader(authHeader);
    return authHeader;
  }

  public JWTBearerToken getToken() throws IOException {
    return executeCall(authApi.tokenPost());
  }

  public TransactionsResponse getTransactions(Long bankId, String accountId, Long companyId) throws IOException {
    return executeCall(bankingApi.transactionsByBankAndAccountGet(bankId, accountId, companyId));
  }

  public AccountResponse getBankAccount(Long bankId, String accountId) throws IOException {
    return executeCall(bankingApi.bankAccountGet(bankId, accountId));
  }

}
