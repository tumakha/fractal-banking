package fractal.banking.service;

import fractal.banking.categorization.CategorizationService;
import fractal.banking.categorization.model.BankTransaction;
import fractal.banking.domain.Category;
import fractal.banking.domain.Transaction;
import fractal.banking.domain.TransactionCategory;
import fractal.banking.repository.CategoryRepository;
import fractal.banking.repository.TransactionCategoryRepository;
import fractal.banking.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static fractal.banking.util.StreamUtil.safeStream;
import static java.lang.String.format;

/**
 * @author Yuriy Tumakha
 */
@Service
public class TransactionCategorization {

  private static final Logger LOG = LoggerFactory.getLogger(TransactionCategorization.class);

  @Autowired
  private CategorizationService categorizationService;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private TransactionCategoryRepository transactionCatRepository;

  @PostConstruct
  public void init() {
    safeStream(categorizationService.getSystemDefinedCategories()).forEach(name -> {
      Category category = new Category();
      category.setName(name);
      categoryRepository.save(category);
    });
  }

  public void improveModel(String prevCategory, String newCategory, Transaction transaction) {
    categorizationService.improveModel(prevCategory, newCategory, convert(transaction));
  }

  public void categorizeTransactions() {
    safeStream(transactionRepository.getWithoutCategory()).forEach(transaction -> {
        try {
          String categoryName = getCategory(transaction);

          Category category = categoryRepository.findById(categoryName)
              .orElseThrow(() -> new IllegalStateException(format("Category '%s' not found", categoryName)));

          TransactionCategory transactionCategory = new TransactionCategory();
          transactionCategory.setTransactionId(transaction.getTransactionId());
          transactionCategory.setCategory(category);
          transactionCategory.setUserDefined(false);

          transactionCatRepository.save(transactionCategory);

        } catch (Exception e) {
          LOG.error("Categorization failed for " + transaction, e);
        }
    });
  }

  private String getCategory(Transaction transaction) {
    return categorizationService.getCategory(convert(transaction));
  }

  private BankTransaction convert(Transaction transaction) {
    BankTransaction trans = new BankTransaction();
    trans.setUserId(getUserId(transaction));
    trans.setMerchant(transaction.getMerchantName());
    trans.setDescription(transaction.getDescription());
    trans.setAmount(transaction.getAmount());
    trans.setCurrencyCode(transaction.getCurrencyCode());
    return trans;
  }

  private String getUserId(Transaction transaction) {
    return format("%s||%s", transaction.getTransactionId().getBankId(), transaction.getAccountId());
  }

}
