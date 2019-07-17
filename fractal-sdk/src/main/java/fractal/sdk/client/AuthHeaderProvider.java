package fractal.sdk.client;

import java.io.IOException;

/**
 * @author Yuriy Tumakha
 */
public interface AuthHeaderProvider {

  String getAuthHeader() throws IOException;

}
