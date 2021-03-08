package br.nubank.models;

import java.time.LocalDateTime;

/**
 * Define model for transaction.
 * We're supposed to parse it from a JSON object
 * e.g.
 * { "transaction": { "merchant": "Burger King", "amount": 20, "time": "2019-02-13T10:00:00.000Z" } }
 *
 */
public class Transaction
{
  private final String merchant;
  private final int amount;
  private final LocalDateTime time;

  public Transaction(String merchant, int amount, LocalDateTime time)
  {
    this.merchant = merchant;
    this.amount = amount;
    this.time = time;
  }

  public String getMerchant()
  {
    return merchant;
  }

  public int getAmount()
  {
    return amount;
  }

  public LocalDateTime getTime()
  {
    return time;
  }

  /**
   * For exploratory purposes, this code block may be removed
   * @return Transaction object as string (pretty print)
   */
  @Override
  public String toString()
  {
    return "transaction: {" +
            "merchant='" + merchant + '\'' +
            ", amount=" + amount +
            ", time=" + time +
            '}';
  }
}
