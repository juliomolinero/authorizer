package br.nubank.services;


import br.nubank.models.Account;
import br.nubank.models.Transaction;
import br.nubank.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static br.nubank.configurations.Constants.*;

/**
 * Helper class that acts as the "glue" between main app and services.
 * It's on charge of processing the lines, convert them as either accounts or transactions,
 * apply validations and create the respective objects that will be stored in their respective
 * repositories.
 *
 * It makes use of the account validation class when creating a transaction
 */
public class OperationService
{
  private static final AccountService accountService = new AccountService();
  private static final TransactionService transactionService = new TransactionService();
  private static final AccountValidation validation = new AccountValidation(accountService, transactionService);

  /**
   * Receives a string and processes it either as an account or transaction
   * @param line Input string, e.g.
   *      Account: { "account": { "activeCard": true, "availableLimit": 100 } }
   *      Transaction: { "transaction": { "merchant": "Burger King", "amount": 20, "time": "2019-02-13T10:00:00.000Z" } }
   *
   * @return Account as string with a list of violations if any, e.g.
   *      { "account": { "activeCard": true, "availableLimit": 80 }, "violations": [] }
   *      { "account": { "activeCard": true, "availableLimit": 80 }, "violations": [ "insufficient-limit" ] }
   *
   */
  public String processLine(String line)
  {
    // Verifies if the input string is an account or a transaction
    // TODO, try to improve this block of code
    if (line.toLowerCase(Locale.ROOT).contains(JSON_ACCOUNT_TYPE)) {
      createAccount(
              Utils.getAccountFromJSONObject(
                      Objects.requireNonNull(Utils.parseToJson(line, JSON_ACCOUNT_TYPE))
              )
      );

    } else if (line.toLowerCase(Locale.ROOT).contains(JSON_TRANSACTION_TYPE)) {
      createTransaction(
              Utils.getTransactionFromJSONObject(
                      Objects.requireNonNull(Utils.parseToJson(line, JSON_TRANSACTION_TYPE))
              )
      );
    }
    try
    {
      // Send account as string
      return accountService.getAccount(0).toString();
    } catch (NullPointerException ex) {
      return "No accounts available at the moment";
    }
  }

  /**
   * Creates an account or add a violation in case the account already exists
   * @param account Account object
   */
  private void createAccount(Account account)
  {
    if (validation.isNewAccount()) {
      accountService.addAccount(account);
    } else {
      List<String> violations = new ArrayList<>(1);
      violations.add(ACCOUNT_ALREADY_INITIALIZED);
      accountService.getAccount(0).setViolations(violations);
    }
  }

  /**
   * a) Creates a transaction and processes it if accomplishes the business rules below:
   * - The transaction amount should not exceed available limit: "insufficient-limit"
   * - No transaction should be accepted when the card is not active: "card-not-active"
   * - There should not be more than 3 transactions on a 2 minute interval: "high-frequency-small-interval"
   * - There should not be more than 2 similar transactions (same amount and merchant)
   *   in a 2 minutes interval: "doubled-transaction"
   *
   * b) IIF everything went well, it subtracts the account available limit with transaction amount e.g.
   * - Account before transaction:
   *   { "account": { "activeCard": true, "availableLimit": 100 } }
   *
   * - Transaction (amount is 20):
   *  { "transaction": { "merchant": "Burger King", "amount": 20, "time": "2019-02-13T10:00:00.000Z" } }
   *
   * - Account after transaction:
   *  { "account": { "activeCard": true, "availableLimit": 80 } }
   *
   * @param transaction Transaction object
   */
  private void createTransaction(Transaction transaction)
  {
    if (validation.isValidAccount()) {
      Account account = accountService.getAccount(0);
      List<String> violations = new ArrayList<>();

      // Perform validations
      if (validation.isCardNotActive()) {
        violations.add(CARD_NOT_ACTIVE);
      }
      if (validation.isInsufficientLimit(transaction)) {
        violations.add(INSUFFICIENT_LIMIT);
      }
      if (validation.isHighFrequencySmallInterval(transaction)) {
        violations.add(HIGH_FREQUENCY_SMALL_INTERVAL);
      }
      if (validation.isDoubledTransaction(transaction)) {
        violations.add(DOUBLED_TRANSACTION);
      }
      // Add transaction and set violations IIF any
      transactionService.addTransaction(transaction);
      account.setViolations(violations);

      // All good ?, subtract available limit with transaction amount
      if (violations.isEmpty()) {
        account.setAvailableLimit((account.getAvailableLimit() - transaction.getAmount()));
      }
    }
  }
}
