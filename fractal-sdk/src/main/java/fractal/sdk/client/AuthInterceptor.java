package fractal.sdk.client;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import java.io.IOException;

/**
 * @author Yuriy Tumakha
 */
public class AuthInterceptor implements Authenticator {

  static final String AUTHORIZATION = "Authorization";

  private AuthHeaderProvider authHeaderProvider;

  AuthInterceptor(AuthHeaderProvider authHeaderProvider) {
    this.authHeaderProvider = authHeaderProvider;
  }

  @Override
  public Request authenticate(Route route, Response response) throws IOException {
    String authorization = authHeaderProvider.getAuthHeader();
    if (!authorization.equals(response.request().header(AUTHORIZATION))) {
      return response.request().newBuilder()
          .header(AUTHORIZATION, authorization)
          .build();
    }
    return null;
  }

}
