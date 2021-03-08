package br.nubank.services;

import br.nubank.models.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

public class TransactionServiceTest
{
  private final TransactionService transactionService = new TransactionService();
  private Transaction transaction;

  @Before
  public void setUp()
  {
    LocalDateTime time = LocalDateTime.parse("2019-02-13T10:00:00.000Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    transaction = new Transaction("Burger King", 40, time);
  }

  @Test
  public void testAddTransactionAndGetMerchantTransactionsIsOne()
  {
    String merchant = "Burger King";
    transactionService.addTransaction(transaction);
    assertEquals(1, transactionService.getMerchantTransactions(merchant).size());
  }

  @Test
  public void testGetMerchantTransactionsIsZero()
  {
    String emptyMerchant = "Merchant";
    assertEquals(0, transactionService.getMerchantTransactions(emptyMerchant).size());
  }
}
