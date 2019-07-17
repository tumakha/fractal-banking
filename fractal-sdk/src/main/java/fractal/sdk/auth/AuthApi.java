package fractal.sdk.auth;

import fractal.sdk.auth.model.JWTBearerToken;
import retrofit2.Call;
import retrofit2.http.POST;

/**
 * @author Yuriy Tumakha
 */
public interface AuthApi {

  /**
   * JWT Auth endpoint.
   *
   * Headers "x-api-key", "x-partner-id" passed by global interceptor
   *
   * @return Call&lt;JWTBearerToken&gt;
   */
  @POST("v1/token")
  Call<JWTBearerToken> tokenPost();

}
