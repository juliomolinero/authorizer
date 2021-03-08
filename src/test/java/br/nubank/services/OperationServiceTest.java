package br.nubank.services;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static br.nubank.configurations.Constants.*;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OperationServiceTest
{
  private final OperationService operationService = new OperationService();
  private String account;

  @Before
  public void setUp()
  {
    account = "{ \"account\": { \"activeCard\": true, \"availableLimit\": 80 } }";
  }

  @Test
  public void testProcessLineAsAccount1OK()
  {
    String result = operationService.processLine(account);
    assertTrue(result.contains("account"));
  }

  @Test
  public void testProcessLineAsAccount2AlreadyInitialized()
  {
    String result = operationService.processLine(account);
    assertTrue(result.contains(ACCOUNT_ALREADY_INITIALIZED));
  }

  @Test
  public void testProcessLineAsTransaction1OK()
  {
    String transaction = "{ \"transaction\": { \"merchant\": \"Burger King\", \"amount\": 20, \"time\": \"2019-02-13T10:00:00.000Z\" } }";
    String result = operationService.processLine(transaction);

    // Reduce account available limit 80 - 20
    assertTrue(result.contains("60"));

    transaction = "{ \"transaction\": { \"merchant\": \"Burger King\", \"amount\": 10, \"time\": \"2019-02-13T10:03:00.000Z\" } }";
    result = operationService.processLine(transaction);

    // Reduce account available limit 60 - 10
    assertTrue(result.contains("50"));
  }

  @Test
  public void testProcessLineAsTransactionInsufficientLimit()
  {
    String transaction = "{ \"transaction\": { \"merchant\": \"Burger King\", \"amount\": 81, \"time\": \"2019-02-13T10:00:00.000Z\" } }";
    String result = operationService.processLine(transaction);

    assertTrue(result.contains(INSUFFICIENT_LIMIT));
  }

  @Test
  public void testProcessLineAsTransactionDoubledTransaction()
  {
    String transaction = "{ \"transaction\": { \"merchant\": \"Burger King\", \"amount\": 81, \"time\": \"2019-02-13T10:00:00.000Z\" } }";
    String result = operationService.processLine(transaction);

    assertTrue(result.contains(INSUFFICIENT_LIMIT));

    transaction = "{ \"transaction\": { \"merchant\": \"Burger King\", \"amount\": 81, \"time\": \"2019-02-13T10:01:00.000Z\" } }";
    result = operationService.processLine(transaction);

    assertTrue(result.contains(DOUBLED_TRANSACTION));
  }

  @Test
  public void testProcessLineAsTransactionHighFrequencySmallInterval()
  {
    String transaction = "{ \"transaction\": { \"merchant\": \"Burger King\", \"amount\": 81, \"time\": \"2019-02-13T10:00:00.000Z\" } }";
    String result = operationService.processLine(transaction);

    assertTrue(result.contains(INSUFFICIENT_LIMIT));

    transaction = "{ \"transaction\": { \"merchant\": \"Burger King\", \"amount\": 81, \"time\": \"2019-02-13T10:01:00.000Z\" } }";
    result = operationService.processLine(transaction);

    assertTrue(result.contains(DOUBLED_TRANSACTION));

    transaction = "{ \"transaction\": { \"merchant\": \"Burger King\", \"amount\": 81, \"time\": \"2019-02-13T10:02:00.000Z\" } }";
    result = operationService.processLine(transaction);

    assertTrue(result.contains(HIGH_FREQUENCY_SMALL_INTERVAL));
  }

  @Test
  public void testFirstProcessLineAsTransactionAccountDoesNotExist()
  {
    String transaction = "{ \"transaction\": { \"merchant\": \"Burger King\", \"amount\": 20, \"time\": \"2019-02-13T10:00:00.000Z\" } }";
    String result = operationService.processLine(transaction);

    assertTrue(result.contains("No accounts available at the moment"));
  }
}
