package br.nubank.services;

import br.nubank.models.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Can be seen it as the Transaction's repository as it performs all the operations
 * related to saving data in memory, adding transaction to a map, etc.
 */
public class TransactionService {
  private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
  /**
   * I decided to use a map so I can get all transactions by merchant and make operations faster
   */
  private final Map<String, List<Transaction>> transactionsMap = new HashMap<>();

  public void addTransaction(Transaction transaction)
  {
    String merchant = transaction.getMerchant();
    List<Transaction> list = transactionsMap.getOrDefault(merchant, new ArrayList<>());
    list.add(transaction);

    transactionsMap.put(merchant, list);
  }

  /**
   * Get a list of transactions by merchant
   * @param merchant Merchant's name e.g. Burger King
   * @return List of transactions
   */
  public List<Transaction> getMerchantTransactions(String merchant)
  {
    return transactionsMap.getOrDefault(merchant, new ArrayList<>());
  }

  /**
   * For exploratory purposes, it may be removed
   */
  public void listTransactions()
  {
    transactionsMap.entrySet()
            .forEach(e ->
                    e.getValue().forEach(a -> logger.info(a.toString())));
  }
}
