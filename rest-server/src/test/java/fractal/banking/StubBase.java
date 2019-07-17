package fractal.banking;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.util.StreamUtils.copyToString;

/**
 * @author Yuriy Tumakha
 */
public abstract class StubBase {

  private static final String X_API_KEY = "x-api-key";
  private static final String X_PARTNER_ID = "x-partner-id";
  private static final String API_KEY = "uv3aFc9ReW56ZuQLHymAwaN5Jht1szWw5uNdvs18";
  private static final String PARTNER_ID = "5111acc7-c9b3-4d2a-9418-16e97b74b1e6";
  private static final String ACCESS_TOKEN =
      "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzYW5kYm94VXNlciIsIm5hbWUiOiJGcmF1308";
  private static final String AUTH_HEADER = "Bearer " + ACCESS_TOKEN;


  protected void expectGetCall(String url, Resource responseBody) throws IOException {
    MappingBuilder request = get(urlEqualTo(url))
        .withHeader(AUTHORIZATION, equalTo(AUTH_HEADER));

    createStub(request, responseBody);
  }

  protected void expectPostCall(String url, Resource responseBody) throws IOException {
    MappingBuilder request = post(urlEqualTo(url));

    createStub(request, responseBody);
  }

  protected void expectPostCall(String url, Resource requestBody, Resource responseBody) throws IOException {
    MappingBuilder request = post(urlEqualTo(url))
        .withRequestBody(equalTo(getResourceAsString(requestBody)))
        .withHeader(CONTENT_TYPE, equalTo(APPLICATION_JSON_VALUE))
        .withHeader(AUTHORIZATION, equalTo(AUTH_HEADER));

    createStub(request, responseBody);
  }

  private void createStub(MappingBuilder request, Resource responseBody) throws IOException {
    stubFor(withGlobalHeaders(request).willReturn(okResponse(responseBody)));
  }

  private MappingBuilder withGlobalHeaders(MappingBuilder mappingBuilder) {
    return mappingBuilder
        .withHeader(ACCEPT, equalTo(APPLICATION_JSON_VALUE))
        .withHeader(X_API_KEY, equalTo(API_KEY))
        .withHeader(X_PARTNER_ID, equalTo(PARTNER_ID));
  }

  private ResponseDefinitionBuilder okResponse(Resource responseBody) throws IOException {
    return aResponse().withStatus(HTTP_OK)
        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .withBody(getResourceAsString(responseBody));
  }

  protected static String getResourceAsString(Resource resource) throws IOException {
    return copyToString(resource.getInputStream(), UTF_8);
  }

}
