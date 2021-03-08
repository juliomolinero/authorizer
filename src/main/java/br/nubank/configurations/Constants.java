package br.nubank.configurations;

/**
 * Defines a series of constants we're using within the program
 */
public class Constants
{
  // Output file name prefix
  public static final String OUT_FILE_NAME_PREFIX = "nubank-auth-";

  // JSONObject types either account or transaction
  public static final String JSON_ACCOUNT_TYPE = "account";
  public static final String JSON_TRANSACTION_TYPE = "transaction";

  // Violations
  public static final String ACCOUNT_ALREADY_INITIALIZED = "account-already-initialized";
  public static final String INSUFFICIENT_LIMIT = "insufficient-limit";
  public static final String CARD_NOT_ACTIVE = "card-not-active";
  public static final String HIGH_FREQUENCY_SMALL_INTERVAL = "high-frequency-small-interval";
  public static final String DOUBLED_TRANSACTION = "doubled-transaction";

  // Avoid creating non functional objects
  private Constants()
  {
  }
}
