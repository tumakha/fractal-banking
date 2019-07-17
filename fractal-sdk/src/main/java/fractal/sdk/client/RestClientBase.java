package fractal.sdk.client;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.net.URL;

import static java.time.Duration.ofSeconds;

/**
 * @author Yuriy Tumakha
 */
public abstract class RestClientBase implements AuthHeaderProvider {

  protected static final String ACCEPT = "Accept";
  protected static final String CONTENT_TYPE = "Content-Type";
  protected static final String APPLICATION_JSON = "application/json";
  private static final int CONNECT_TIMEOUT = 3;
  private static final int READ_TIMEOUT = 5;
  private static final Level DEBUG_LEVEL = Level.BODY;

  private OkHttpClient httpClient;
  private Converter.Factory converterFactory;
  private HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
  protected ChainInterceptor chainInterceptor;

  protected RestClientBase(Headers globalHeaders) {
    chainInterceptor = new ChainInterceptor(globalHeaders);
    httpClient = createHttpClient();
    converterFactory = JacksonConverterFactory.create(jsonObjectMapper());
  }

  protected <T> T createService(URL baseUrl, final Class<T> service) {
    return new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(converterFactory)
        .client(httpClient)
        .build()
        .create(service);
  }

  private OkHttpClient createHttpClient() {
    return new OkHttpClient.Builder()
        .connectTimeout(ofSeconds(CONNECT_TIMEOUT))
        .readTimeout(ofSeconds(READ_TIMEOUT))
        .addInterceptor(chainInterceptor)
        .authenticator(new AuthInterceptor(this))
        .addNetworkInterceptor(loggingInterceptor)
        .build();
  }

  private ObjectMapper jsonObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);
    mapper.setSerializationInclusion(Include.NON_NULL);
    return mapper;
  }

  protected <T> T executeCall(Call<T> call) throws IOException {
    Response<T> response = call.execute();

    if (!response.isSuccessful())
      throw new IOException(response.errorBody().string());

    return response.body();
  }

  public boolean isDebugEnabled() {
    return loggingInterceptor.getLevel() == DEBUG_LEVEL;
  }

  public void setDebug(boolean debug) {
    loggingInterceptor.setLevel(debug? DEBUG_LEVEL : Level.NONE);
  }

}
