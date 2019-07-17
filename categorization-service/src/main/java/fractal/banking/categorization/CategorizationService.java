package fractal.banking.categorization;

import fractal.banking.categorization.model.BankTransaction;
import fractal.banking.categorization.model.CategoryRelevance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * @author Yuriy Tumakha
 */
public class CategorizationService {

  private String defaultCategory;
  private static final int USER_DEFINED_WORDS_MULTIPLIER = 100;
  private final Map<String, Map<String, Long>> category2Words = new CategoryWordsMapBuilder().build();
  private final Map<String, Map<String, Map<String, Long>>> userDefinedMap = new ConcurrentHashMap<>();

  public CategorizationService(String defaultCategory) {
    assert defaultCategory != null : "Default category can't be null";
    this.defaultCategory = defaultCategory;
  }

  public List<String> getSystemDefinedCategories() {
    List<String> categories = new ArrayList<>(category2Words.keySet());
    categories.add(defaultCategory);
    return categories;
  }

  public String getCategory(BankTransaction bankTransaction) {
    Map<String, Long> transactionWordsCount = getWordsCount(bankTransaction);

    Map<String, Map<String, Long>> catWordMap = getCategoryWordsMapForUser(bankTransaction.getUserId());

    return catWordMap.entrySet().stream().map(entry -> {
      Map<String, Long> catWordCounts = entry.getValue();

      long catRelevance = transactionWordsCount.entrySet().stream()
          .mapToLong(e -> catWordCounts.getOrDefault(e.getKey(), 0L) * e.getValue()).sum();

      return new CategoryRelevance(entry.getKey(), catRelevance);
    }).filter(cr -> cr.getRelevance() > 10)
        .max(comparing(CategoryRelevance::getRelevance))
        .map(CategoryRelevance::getCategory).orElse(defaultCategory);
  }

  public void improveModel(String prevCategory, String newCategory, BankTransaction bankTransaction) {
    Map<String, Map<String, Long>> catWordMap = getCategoryWordsMapForUser(bankTransaction.getUserId());

    Map<String, Long> transactionWordsCount = getWordsCount(bankTransaction);

    synchronized (catWordMap) {
      catWordMap.putIfAbsent(prevCategory, new HashMap<>());
      catWordMap.putIfAbsent(newCategory, new HashMap<>());
      Map<String, Long> prevWordCountMap = catWordMap.get(prevCategory);
      Map<String, Long> newWordCountMap = catWordMap.get(newCategory);

      transactionWordsCount.forEach((tWord, tCount) -> {
        Long tWordCount = USER_DEFINED_WORDS_MULTIPLIER * tCount;
        prevWordCountMap.compute(tWord, (w, c) -> c == null ? -tWordCount : c - tWordCount);
        newWordCountMap.compute(tWord, (w, c) -> c == null ? tWordCount : c + tWordCount);
      });
    }
  }

  private Map<String, Map<String, Long>> getCategoryWordsMapForUser(String userId) {
    userDefinedMap.computeIfAbsent(userId, this::cloneSystemCatWordMap);
    return userDefinedMap.get(userId);
  }

  private Map<String, Map<String, Long>> cloneSystemCatWordMap(String userId) {
    HashMap<String, Map<String, Long>> map = new HashMap<>();
    category2Words.forEach((category, wordCountMap) ->
      map.put(category, new HashMap<>(wordCountMap))
    );
    return map;
  }

  private  Map<String, Long> getWordsCount(BankTransaction tr) {
    String sentence = tr.getDescription() + " " + tr.getMerchant() + " " + tr.getMerchant();
    return Stream.of(sentence.split("\\W+"))
        .filter(w -> w.length() > 2 && !w.matches("^\\d+$"))
        .map(String::toLowerCase)
        .collect(groupingBy(identity(), counting()));
  }

}
