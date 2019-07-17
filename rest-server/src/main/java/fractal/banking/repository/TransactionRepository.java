package fractal.banking.repository;

import fractal.banking.domain.Transaction;
import fractal.banking.domain.TransactionId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author Yuriy Tumakha
 */
@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, TransactionId> {

  @Transactional(readOnly = true)
  @Query("SELECT t FROM Transaction t WHERE t.transactionId.bankId = :bankId AND t.accountId = :accountId " +
      " AND t.transactionCategory.category.name = :category ORDER BY t.bookingDate")
  Page<Transaction> findByBankIdAndAccountIdAndCategory(
      @Param("bankId") Long bankId,
      @Param("accountId") String accountId,
      @Param("category") String category,
      Pageable pageable);

  @Transactional(readOnly = true)
  @Query("SELECT t FROM Transaction t WHERE t.transactionId.bankId = :bankId AND t.accountId = :accountId " +
      " ORDER BY t.bookingDate")
  Page<Transaction> findByBankIdAndAccountId(
      @Param("bankId") Long bankId,
      @Param("accountId") String accountId,
      Pageable pageable);

  @Transactional(readOnly = true)
  @Query("SELECT t FROM Transaction t LEFT JOIN t.transactionCategory tc WHERE tc.category IS NULL")
  List<Transaction> getWithoutCategory();

}
