package fractal.banking.categorization;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

/**
 * @author Yuriy Tumakha
 */
public class CategoryWordsMapBuilderTest {

  @Test
  public void testCategoryWordsMap() {
    Map<String, Map<String, Long>> categoryWordsMap = new CategoryWordsMapBuilder().build();

    assertThat(categoryWordsMap.keySet(), hasItems("Coffee", "Travel", "Eating Out"));
    assertThat(categoryWordsMap.get("Coffee").keySet(), hasItems("costa", "starbucks"));
    assertThat(categoryWordsMap.get("Coffee").get("costa"), equalTo(500L));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testMapIsImmutable() {
    Map<String, Map<String, Long>> categoryWordsMap = new CategoryWordsMapBuilder().build();
    categoryWordsMap.put("Travel", new HashMap<>());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testMapValuesAreImmutable() {
    Map<String, Map<String, Long>> categoryWordsMap = new CategoryWordsMapBuilder().build();
    categoryWordsMap.get("Travel").put("space", 200L);
  }

}
