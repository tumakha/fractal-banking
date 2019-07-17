package fractal.banking.repository;

import fractal.banking.domain.TransactionCategory;
import fractal.banking.domain.TransactionId;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Yuriy Tumakha
 */
@Repository
public interface TransactionCategoryRepository extends PagingAndSortingRepository<TransactionCategory, TransactionId> {
}