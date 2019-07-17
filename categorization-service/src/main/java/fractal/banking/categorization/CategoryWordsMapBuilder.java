package fractal.banking.categorization;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * @author Yuriy Tumakha
 */
public class CategoryWordsMapBuilder {

  private Map<String, Map<String, Long>> category2Words = new HashMap<>();

  public Map<String, Map<String, Long>> build() {
    addSystemCategories();
    return unmodifiableMap(category2Words);
  }

  private void addSystemCategories() {
    addCateogory("Coffee",  new HashMap<String, Long>() {{
      put("coffee", 500L);
      put("costa", 500L);
      put("starbucks", 500L);
      put("nero", 400L);
      put("nespresso", 400L);
      put("espresso", 500L);
      put("late", 500L);
    }});

    addCateogory("Travel",  new HashMap<String, Long>() {{
      put("tfl", 500L);
      put("car", 400L);
      put("cab", 400L);
      put("taxi", 500L);
      put("uber", 400L);
      put("bike", 400L);
    }});

    addCateogory("Salary",  new HashMap<String, Long>() {{
      put("payroll", 500L);
      put("salary", 500L);
      put("wage", 300L);
    }});

    addCateogory("Eating Out",  new HashMap<String, Long>() {{
      put("restaurant", 500L);
      put("cafe", 500L);
      put("food", 400L);
      put("eat", 400L);
    }});

    addCateogory("Shopping",  new HashMap<String, Long>() {{
      put("shop", 500L);
      put("retail", 500L);
      put("amazon", 500L);
    }});

  }

  private void addCateogory(String category, Map<String, Long> wordsWithCount) {
    category2Words.put(category, unmodifiableMap(wordsWithCount));
  }

}
