package br.nubank.services;

import br.nubank.models.Account;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountServiceTest
{
  private final AccountService accountService = new AccountService();
  private Account account;

  @Before
  public void setUp()
  {
    account = new Account(true, 200);
  }

  @Test
  public void testAddAccountListSizeShouldBeOne()
  {
    accountService.addAccount(account);
    assertEquals(1, accountService.getAccountListSize());
  }

  @Test
  public void testGetAccountListSizeIsZero()
  {
    assertEquals(0, accountService.getAccountListSize());
  }

  @Test
  public void testGetAccount()
  {
    accountService.addAccount(account);
    Account newAccount = accountService.getAccount(0);

    assertTrue(newAccount.isActiveCard());
    assertEquals(200, newAccount.getAvailableLimit());
  }

  @Test
  public void testGetAccountIsNull()
  {
    Account newAccount = accountService.getAccount(0);
    assertTrue(Objects.isNull(newAccount));
  }
}
