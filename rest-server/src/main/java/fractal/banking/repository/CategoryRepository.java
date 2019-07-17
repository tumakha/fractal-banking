package fractal.banking.repository;

import fractal.banking.domain.Category;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Yuriy Tumakha
 */
@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, String> {
}
