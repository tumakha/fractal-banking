package fractal.banking.service;

import fractal.banking.domain.Transaction;
import fractal.banking.domain.TransactionId;
import fractal.banking.repository.TransactionRepository;
import fractal.sdk.FractalSDK;
import fractal.sdk.banking.model.AccountResponse;
import fractal.sdk.banking.model.Merchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static fractal.banking.util.StreamUtil.safeStream;
import static java.util.Optional.ofNullable;
import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * @author Yuriy Tumakha
 */
@Service
public class BankService {

  @Autowired
  private FractalSDK fractalSDK;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private TransactionCategorization transactionCategorization;

  public AccountResponse getBankAccount(Long bankId, String accountId) throws IOException {
    return fractalSDK.getBankAccount(bankId, accountId);
  }

  @Async
  public void syncAccountTransactions(AccountResponse acc) throws IOException {
    safeStream(fractalSDK.getTransactions(acc.getBankId(), acc.getAccountId(), null).getResults())
        .forEach(t -> {
          Transaction transaction = new Transaction();
          copyProperties(t, transaction);
          transaction.setTransactionId(new TransactionId(t.getBankId(), t.getTransactionId()));
          transaction.setMerchantName(ofNullable(t.getMerchant()).map(Merchant::getName).orElse(""));

          transactionRepository.save(transaction);
        });

    transactionCategorization.categorizeTransactions();
  }

}
