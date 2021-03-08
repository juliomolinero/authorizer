package br.nubank.models;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

/**
 * Perform tests against the Transaction model
 */
public class TransactionTest
{

  @Test
  public void testConstructors()
  {
    LocalDateTime time = LocalDateTime.parse("2019-02-13T10:00:00.000Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    Transaction transaction = new Transaction("Burger King", 20, time);

    assertEquals(true, transaction instanceof Transaction);
    assertEquals("Burger King", transaction.getMerchant());
    assertEquals(20, transaction.getAmount());
    assertEquals(time, transaction.getTime());
  }

  @Test
  public void testGetters()
  {
    LocalDateTime time = LocalDateTime.parse("2019-02-13T11:00:00.000Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    Transaction transaction = new Transaction("Habbib's", 90, time);

    assertEquals("Habbib's", transaction.getMerchant());
    assertEquals(90, transaction.getAmount());
    assertEquals(time, transaction.getTime());
  }

  @Test
  public void testToString()
  {
    LocalDateTime time = LocalDateTime.parse("2019-02-13T11:00:00.000Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    Transaction transaction = new Transaction("Habbib's", 90, time);

    String expected = "transaction: {merchant='Habbib's', amount=90, time=2019-02-13T11:00}";
    String result = transaction.toString();

    assertEquals(expected, result);
  }
}
