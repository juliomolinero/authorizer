package br.nubank.services;

import br.nubank.models.Transaction;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Helper class to perform all validations a transaction must accomplish
 * before being added to the transactions list
 */
public class AccountValidation
{
  private final AccountService accountService;
  private final TransactionService transactionService;

  public AccountValidation(AccountService accountService, TransactionService transactionService)
  {
    this.accountService = accountService;
    this.transactionService = transactionService;
  }

  /**
   * Is new account when account list is empty
   * @return Boolean
   */
  public boolean isNewAccount()
  {
    return accountService.getAccountListSize() == 0;
  }

  /**
   * Is card not active when account active card is false
   * e.g. { "account": { "activeCard": false, "availableLimit": 100 } }
   * @return Boolean
   */
  public boolean isCardNotActive()
  {
    if (isValidAccount()) {
      return !accountService.getAccount(0).isActiveCard();
    } else {
      return true;
    }
  }

  /**
   * Is insufficient limit when account available limit is less than
   * current transaction amount e.g.
   * Account: { "account": { "activeCard": false, "availableLimit": 20 } }
   *
   * Current Transaction:
   * { "transaction": { "merchant": "Burger King", "amount": 100, "time": "2019-02-13T10:00:00.000Z" } }
   *
   * @param transaction Current transaction
   * @return Boolean
   */
  public boolean isInsufficientLimit(Transaction transaction)
  {
    if (isValidAccount()) {
      return transaction.getAmount() > accountService.getAccount(0).getAvailableLimit();
    } else {
      return true;
    }
  }

  /**
   * Is doubled transaction when trying to process transactions from the same merchant, amount
   * and within 2 minutes interval e.g.
   * - Habbib's transactions:
   * { "transaction": { "merchant": "Habbib's", "amount": 90, "time": "2019-02-13T11:00:00.000Z" } }
   *
   * - Current transaction (about to be processed), notice time is one minute after
   *   the last transaction for Hobbib's
   * { "transaction": { "merchant": "Habbib's", "amount": 90, "time": "2019-02-13T11:01:00.000Z" } }
   *
   * @param transaction Current transaction
   * @return Boolean
   */
  public boolean isDoubledTransaction(Transaction transaction)
  {
    if (isValidAccount()) {
      String merchant = transaction.getMerchant();
      // Are there any other transactions?
      if (!transactionService.getMerchantTransactions(merchant).isEmpty()) {
        return sameTransactions(transaction) > 0;
      } else {
        return false;
      }
    } else {
      return true;
    }
  }

  /**
   * Is high frequency small interval transaction when trying to process more than 3
   * transactions within 2 minutes interval e.g.
   * - Habbib's transactions:
   * { "transaction": { "merchant": "Habbib's", "amount": 90, "time": "2019-02-13T11:00:00.000Z" } }
   * { "transaction": { "merchant": "Habbib's", "amount": 90, "time": "2019-02-13T11:01:00.000Z" } }
   *
   * - Current transaction:
   * { "transaction": { "merchant": "Habbib's", "amount": 90, "time": "2019-02-13T11:02:00.000Z" } }
   *
   * @param transaction Current transaction
   * @return Boolean
   */
  public boolean isHighFrequencySmallInterval(Transaction transaction)
  {
    if (isValidAccount()) {
      return highVolumeTransactions(transaction) >= 3;
    } else {
      return true;
    }
  }

  /**
   * Is valid account when account exists in the list of accounts
   * (no need to have more than one at the moment)
   * @return Boolean
   */
  public boolean isValidAccount()
  {
    return !Objects.isNull(accountService.getAccount(0));
  }

  private long differenceInMinutes(LocalDateTime lastTransaction, LocalDateTime currentTransaction)
  {
    return Math.abs(ChronoUnit.MINUTES.between(lastTransaction, currentTransaction));
  }

  private int sameTransactions(Transaction transaction)
  {
    List<Transaction> transactions = transactionService.getMerchantTransactions(transaction.getMerchant())
            .stream()
            .filter( t -> differenceInMinutes(t.getTime(), transaction.getTime()) <= 2 )
            .filter( t -> t.getAmount() == transaction.getAmount() )
            .collect(Collectors.toList());

    return transactions.size();
  }

  private int highVolumeTransactions(Transaction transaction)
  {
    List<Transaction> transactions = transactionService.getMerchantTransactions(transaction.getMerchant())
            .stream()
            .filter( t -> differenceInMinutes(t.getTime(), transaction.getTime()) <= 2 )
            .collect(Collectors.toList());

    return transactions.size();
  }
}
