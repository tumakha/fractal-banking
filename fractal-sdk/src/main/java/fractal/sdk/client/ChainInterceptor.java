package fractal.sdk.client;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import static fractal.sdk.client.AuthInterceptor.AUTHORIZATION;

/**
 * @author Yuriy Tumakha
 */
public class ChainInterceptor implements Interceptor {

  private Headers globalHeaders;
  private String authHeader = "";

  ChainInterceptor(Headers globalHeaders) {
    this.globalHeaders = globalHeaders;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Headers headers = globalHeaders.newBuilder()
        .addAll(chain.request().headers())
        .add(AUTHORIZATION, authHeader)
        .build();
    Request request = chain.request().newBuilder().headers(headers).build();
    return chain.proceed(request);
  }

  public void setAuthHeader(String authHeader) {
    this.authHeader = authHeader;
  }

}
