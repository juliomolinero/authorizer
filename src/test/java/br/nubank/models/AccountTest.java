package br.nubank.models;

import br.nubank.configurations.Constants;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Performs tests against the Account model
 */
public class AccountTest
{

  @Test
  public void testConstructor()
  {
    Account account = new Account(true, 100);

    assertEquals(true, account instanceof Account);
    assertEquals(true, account.isActiveCard());
    assertEquals(100, account.getAvailableLimit());
    assertEquals(0, account.getViolations().size());
  }

  @Test
  public void testSettersAndGetters()
  {
    Account account = new Account(false, 100);
    List<String> violations = new ArrayList<>(2);
    violations.add(Constants.CARD_NOT_ACTIVE);
    violations.add(Constants.ACCOUNT_ALREADY_INITIALIZED);

    account.setAvailableLimit(60);
    account.setViolations(violations);

    assertEquals(60, account.getAvailableLimit());
    assertEquals(2, account.getViolations().size());
    assertEquals(1, account.getViolations()
            .stream()
            .filter(v -> v.contains(Constants.CARD_NOT_ACTIVE))
            .count());

  }
}
