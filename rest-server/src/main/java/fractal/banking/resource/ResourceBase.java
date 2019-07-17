package fractal.banking.resource;

import fractal.banking.dto.PageWrapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Yuriy Tumakha
 */
abstract class ResourceBase {

  <T> ResponseEntity<PageWrapper<T>> page(Page<T> page) {
    return ok(new PageWrapper<>(page));
  }

  ResponseStatusException errorResponse(HttpStatus status, String reason) {
    return new ResponseStatusException(status, reason);
  }

}
