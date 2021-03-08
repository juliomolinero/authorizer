package br.nubank.services;

import br.nubank.models.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Can be seen it as the Account's repository as it performs all the operations
 * related to saving data in memory, adding account to a list, etc.
 */
public class AccountService
{
  private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
  // No need to store more than one account at the moment
  private final List<Account> accounts = new ArrayList<>(1);

  public void addAccount(Account account)
  {
    accounts.add(account);
  }

  public int getAccountListSize()
  {
    return accounts.size();
  }

  public Account getAccount(int index)
  {
    try
    {
      return accounts.get(index);
    } catch (IndexOutOfBoundsException ex) {
      logger.error(String.format("Account with index %d does not exist. %s", index, ex.getMessage()));
    }
    return null;
  }

  /**
   * For exploratory purposes, it may be removed
   */
  public void listAccounts()
  {
    accounts.forEach(a ->  logger.info(a.toString()));
  }
}