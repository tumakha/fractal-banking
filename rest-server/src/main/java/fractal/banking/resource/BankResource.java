package fractal.banking.resource;

import fractal.banking.domain.Category;
import fractal.banking.domain.Transaction;
import fractal.banking.domain.TransactionCategory;
import fractal.banking.domain.TransactionId;
import fractal.banking.dto.*;
import fractal.banking.repository.CategoryRepository;
import fractal.banking.repository.TransactionCategoryRepository;
import fractal.banking.repository.TransactionRepository;
import fractal.banking.service.BankService;
import fractal.banking.service.TransactionCategorization;
import fractal.sdk.banking.model.AccountResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.IOException;

import static fractal.banking.dto.CreateCategoryDTO.CATEGORY_MAX_LENGTH;
import static java.lang.String.format;
import static java.net.HttpURLConnection.*;
import static java.util.Optional.ofNullable;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author Yuriy Tumakha
 */
@RestController
@Validated
@RequestMapping(path = "v1/bank", produces = APPLICATION_JSON_UTF8_VALUE)
public class BankResource extends ResourceBase {

  private static final Logger LOG = LoggerFactory.getLogger(BankResource.class);

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private TransactionCategoryRepository transactionCatRepository;

  @Autowired
  private BankService bankService;

  @Autowired
  private TransactionCategorization transactionCategorization;

  @RequestMapping(path = "{bankId}/account/{accountId}/transaction", method = GET)
  @ApiOperation("Get account transactions")
  @ApiResponses(value = {
      @ApiResponse(code = HTTP_BAD_REQUEST, message = "Bad request. Bank or Account not found"),
      @ApiResponse(code = HTTP_INTERNAL_ERROR, message = "Internal Server Error")
  })
  public ResponseEntity<PageWrapper<TransactionDTO>> getAccountTransactions(
      @PathVariable("bankId") @Min(1) Long bankId,
      @PathVariable("accountId") @Size(max = 100) String accountId,

      @Size(max = CATEGORY_MAX_LENGTH)
      @RequestParam(name = "category", required = false) String category,

      @Min(0)
      @ApiParam("Zero-based page index")
      @RequestParam(name = "page", defaultValue = "0") int page,

      @Range(min = 1, max = 200)
      @ApiParam("Size of page to be returned")
      @RequestParam(name = "size", defaultValue = "50") int size) {

    if (category == null) {
      return page(transactionRepository
          .findByBankIdAndAccountId(bankId, accountId, of(page, size)).map(TransactionDTO::of));
    } else {
      categoryRepository.findById(category)
          .orElseThrow(() -> errorResponse(BAD_REQUEST, format("Category '%s' not found", category)));

      return page(transactionRepository
          .findByBankIdAndAccountIdAndCategory(bankId, accountId, category, of(page, size)).map(TransactionDTO::of));
    }
  }

  @RequestMapping(path = "{bankId}/account/{accountId}/transaction/sync", method = PUT)
  @ApiOperation("Start account transactions synchronization from Fractal")
  @ApiResponses(value = {
      @ApiResponse(code = HTTP_ACCEPTED, message = "Sync request accepted"),
      @ApiResponse(code = HTTP_BAD_REQUEST, message = "Bad request. Bank or Account not found"),
      @ApiResponse(code = HTTP_INTERNAL_ERROR, message = "Internal Server Error")
  })
  public ResponseEntity<TransactionsSyncStartedDTO> syncTransactionsFromFractal(
      @PathVariable("bankId") @Min(1) Long bankId,
      @PathVariable("accountId") @Size(max = 100) String accountId) throws IOException {

    AccountResponse acc = bankService.getBankAccount(bankId, accountId);
    bankService.syncAccountTransactions(acc);

    return accepted().body(TransactionsSyncStartedDTO.of("Account transactions synchronization started", acc));
  }

  @RequestMapping(path = "{bankId}/transaction/{transactionId}/category", method = PATCH)
  @ApiOperation("Add or Change transaction category")
  @ApiResponses(value = {
      @ApiResponse(code = HTTP_BAD_REQUEST, message = "Bad request or Category not found"),
      @ApiResponse(code = HTTP_INTERNAL_ERROR, message = "Internal Server Error")
  })
  public ResponseEntity<TransactionCategoryDTO> addOrChangeTransactionCategory(
      @PathVariable("bankId") @Min(1) Long bankId,
      @PathVariable("transactionId") @Size(max = 100) String transactionId,
      @Valid @RequestBody ChangeTransactionCategoryDTO changeDTO) {

    String categoryName = changeDTO.getCategory();

    Category category = categoryRepository.findById(categoryName)
        .orElseThrow(() -> errorResponse(BAD_REQUEST, format("Category '%s' not found", categoryName)));

    TransactionId transactionIdKey = new TransactionId(bankId, transactionId);
    Transaction dbTransaction = transactionRepository.findById(transactionIdKey)
        .orElseThrow(() -> errorResponse(BAD_REQUEST, format("Transaction not found by %s", transactionIdKey)));

    String dbCategory = ofNullable(dbTransaction.getTransactionCategory())
        .map(tc -> tc.getCategory().getName()).orElse("");
    if (!categoryName.equals(dbCategory)) {
      transactionCategorization.improveModel(dbCategory, categoryName, dbTransaction);
    }

    TransactionCategory transactionCategory = new TransactionCategory();
    transactionCategory.setTransactionId(transactionIdKey);
    transactionCategory.setCategory(category);
    transactionCategory.setUserDefined(true);

    return ok(TransactionCategoryDTO.of(transactionCatRepository.save(transactionCategory)));
  }

}
