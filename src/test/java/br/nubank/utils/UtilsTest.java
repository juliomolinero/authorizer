package br.nubank.utils;

import br.nubank.models.Account;
import br.nubank.models.Transaction;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static br.nubank.configurations.Constants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class UtilsTest
{
  private String account;
  private String transaction;

  @Before
  public void setUp()
  {
    account = "{ \"account\": { \"activeCard\": true, \"availableLimit\": 80 } }";
    transaction = "{ \"transaction\": { \"merchant\": \"Burger King\", \"amount\": 20, \"time\": \"2019-02-13T10:00:00.000Z\" } }";
  }

  @Test
  public void testParseToJsonAccount()
  {
    JSONObject jsonObject = Utils.parseToJson(account, JSON_ACCOUNT_TYPE);

    // Check account has been parsed
    assertTrue(jsonObject.has("activeCard"));
    assertTrue(jsonObject.has("availableLimit"));

    // Check values are correct
    assertTrue(jsonObject.getBoolean("activeCard"));
    assertEquals(80, jsonObject.getInt("availableLimit"));
  }

  @Test
  public void testParseToJsonTransaction()
  {
    JSONObject jsonObject = Utils.parseToJson(transaction, JSON_TRANSACTION_TYPE);

    // Check transaction has been parsed
    assertTrue(jsonObject.has("merchant"));
    assertTrue(jsonObject.has("amount"));
    assertTrue(jsonObject.has("time"));

    // Check values are correct
    assertEquals("Burger King", jsonObject.getString("merchant"));
    assertEquals(20, jsonObject.getInt("amount"));
    assertEquals("2019-02-13T10:00:00.000Z", jsonObject.getString("time"));
  }

  @Test
  public void testGetAccountFromJSONObject()
  {
    JSONObject jsonObject = Utils.parseToJson(account, JSON_ACCOUNT_TYPE);
    Account account = Utils.getAccountFromJSONObject(jsonObject);

    // Check is a valid object
    assertTrue(account instanceof Account);
  }

  @Test(expected = NullPointerException.class)
  public void testGetAccountFromJSONObjectNullPointerExceptionExpected()
  {
    JSONObject jsonObject = Utils.parseToJson(account, JSON_TRANSACTION_TYPE);
    Account account = Utils.getAccountFromJSONObject(jsonObject);
  }

  @Test
  public void testGetTransactionFromJSONObject()
  {
    JSONObject jsonObject = Utils.parseToJson(transaction, JSON_TRANSACTION_TYPE);
    Transaction transaction = Utils.getTransactionFromJSONObject(jsonObject);

    // Check is a valid object
    assertTrue(transaction instanceof Transaction);
  }

  @Test(expected = NullPointerException.class)
  public void testGetTransactionFromJSONObjectNullPointerExceptionExpected()
  {
    JSONObject jsonObject = Utils.parseToJson(transaction, JSON_ACCOUNT_TYPE);
    Transaction transaction = Utils.getTransactionFromJSONObject(jsonObject);
  }

  @Test
  public void testGetAccountFromJSONObjectAssertThrowsException()
  {
    assertThrows(NullPointerException.class,
            () -> {
              JSONObject jsonObject = Utils.parseToJson(account, JSON_TRANSACTION_TYPE);
              Account account = Utils.getAccountFromJSONObject(jsonObject);
            });
  }
}
