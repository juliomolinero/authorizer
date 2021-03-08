package br.nubank.utils;

import br.nubank.models.Account;
import br.nubank.models.Transaction;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Helper class in charge of performing more general tasks like
 * parsing JSONObjects or creating accounts and / or transactions
 * from JSON strings
 */
public class Utils
{
  private static final Logger logger = LoggerFactory.getLogger(Utils.class);

  // Avoid creating non functional objects
  private Utils()
  {
  }

  /**
   * Tries to parse a string into a JSONObject of an specific object type [account | transaction]
   * @param line Input String e.g.
   *        - String 1 { "account": { "activeCard": true, "availableLimit": 80 } }
   *        - String 2 { "transaction": { "merchant": "Burger King", "amount": 20, "time": "2019-02-13T10:00:00.000Z" } }
   * @param type Object type [account | transaction]
   * @return JSONObject
   */
  public static JSONObject parseToJson(String line, String type) {
    try
    {
      JSONObject jsonObject = new JSONObject(line);
      return jsonObject.getJSONObject(type);

    } catch (JSONException ex) {
      logger.error(type + " could not be parsed to a JSONObject " + ex.getMessage());
    }
    return null;
  }

  /**
   * Creates an account from a JSONObject
   * @param jsonObject Input JSONObject e.g.
   *        { "account": { "activeCard": true, "availableLimit": 80 } }
   * @return Account
   */
  public static Account getAccountFromJSONObject(JSONObject jsonObject) {
    boolean activeCard = jsonObject.getBoolean("activeCard");
    int availableLimit = jsonObject.getInt("availableLimit");

    return new Account(activeCard, availableLimit);
  }

  /**
   * Creates a transaction from a JSONObject
   * @param jsonObject Input JSONObject e.g.
   *        { "transaction": { "merchant": "Burger King", "amount": 20, "time": "2019-02-13T10:00:00.000Z" } }
   * @return Transaction
   */
  public static Transaction getTransactionFromJSONObject(JSONObject jsonObject) {
    String merchant = jsonObject.getString("merchant");
    int amount = jsonObject.getInt("amount");
    LocalDateTime time = LocalDateTime.parse(jsonObject.getString("time"), DateTimeFormatter.ISO_OFFSET_DATE_TIME);

    return new Transaction(merchant, amount, time);
  }
}
