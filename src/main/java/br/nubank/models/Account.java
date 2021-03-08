package br.nubank.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Define model for account.
 * We're supposed to parse it from a JSON object
 * e.g.
 * { "account": { "activeCard": true, "availableLimit": 100 } }
 *
 */
public class Account
{
  private final boolean activeCard;
  private int availableLimit;
  private List<String> violations;

  public Account(boolean activeCard, int availableLimit)
  {
    this.activeCard = activeCard;
    this.availableLimit = availableLimit;
    this.violations = new ArrayList<>();
  }

  public boolean isActiveCard()
  {
    return activeCard;
  }

  public int getAvailableLimit()
  {
    return availableLimit;
  }

  public void setAvailableLimit(int availableLimit)
  {
    this.availableLimit = availableLimit;
  }

  public List<String> getViolations()
  {
    return violations;
  }

  public void setViolations(List<String> violations)
  {
    this.violations = violations;
  }

  /**
   * Define the way an account must be printed out when sending it as a response
   * @return Account object as string (pretty print)
   */
  @Override
  public String toString()
  {
    String violation = violations.stream()
            .map(v -> " \"" + v + "\" ")
            .collect(Collectors.joining(",", "[", "]"));

    return "{ account: { \"activeCard\": " + activeCard + ", \"availableLimit\": " + availableLimit + " }, "
      + "\"violations\": " + violation + " }";
  }
}
