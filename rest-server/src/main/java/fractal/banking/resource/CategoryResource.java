package fractal.banking.resource;

import fractal.banking.dto.CategoryDTO;
import fractal.banking.dto.CreateCategoryDTO;
import fractal.banking.dto.PageWrapper;
import fractal.banking.domain.Category;
import fractal.banking.repository.CategoryRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import static java.lang.String.format;
import static java.net.HttpURLConnection.*;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Yuriy Tumakha
 */
@RestController
@Validated
@RequestMapping(path = "v1/category", produces = APPLICATION_JSON_UTF8_VALUE)
public class CategoryResource extends ResourceBase {

  private static final Logger LOG = LoggerFactory.getLogger(CategoryResource.class);

  @Autowired
  private CategoryRepository categoryRepository;

  @RequestMapping(method = POST)
  @ApiOperation("Add user defined category")
  @ApiResponses(value = {
      @ApiResponse(code = HTTP_CREATED, message = "Category created"),
      @ApiResponse(code = HTTP_CONFLICT, message = "Conflict. Category already exists"),
      @ApiResponse(code = HTTP_BAD_REQUEST, message = "Bad request"),
      @ApiResponse(code = HTTP_INTERNAL_ERROR, message = "Internal Server Error")
  })
  public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CreateCategoryDTO createCategoryDTO) {
    String name = createCategoryDTO.getName();

    Category category = new Category();
    category.setName(name);
    category.setUserDefined(true);

    if (categoryRepository.findById(name).isPresent())
      throw new ResponseStatusException(CONFLICT, format("Category '%s' already exists", name));

    return status(CREATED).body(CategoryDTO.of(categoryRepository.save(category)));
  }

  @RequestMapping(method = GET)
  @ApiOperation("Get categories")
  public ResponseEntity<PageWrapper<CategoryDTO>> getCategories(

      @Min(0)
      @ApiParam("Zero-based page index")
      @RequestParam(name = "page", defaultValue = "0") Integer page,

      @Range(min = 1, max = 200)
      @ApiParam("Size of page to be returned")
      @RequestParam(name = "size", defaultValue = "50") Integer size) {

    return page(categoryRepository.findAll(of(page, size, Sort.by("name"))).map(CategoryDTO::of));
  }

}
