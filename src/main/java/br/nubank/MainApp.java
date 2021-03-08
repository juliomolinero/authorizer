package br.nubank;

import br.nubank.configurations.Constants;
import br.nubank.services.OperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainApp {
  private static final Logger logger = LoggerFactory.getLogger(MainApp.class);
  private static final OperationService service = new OperationService();

  /**
   * Main entry point, receives an input file and processes it line by line
   *
   * @param args Input file, e.g.
   * $ cat operations
   *  { "account": { "activeCard": true, "availableLimit": 100 } }
   *  { "transaction": { "merchant": "Burger King", "amount": 20, "time": "2019-02-13T10:00:00.000Z" } }
   *  { "transaction": { "merchant": "Burger King", "amount": 20, "time": "2019-02-13T10:03:00.000Z" } }
   *  { "transaction": { "merchant": "Burger King", "amount": 20, "time": "2019-02-13T10:06:00.000Z" } }
   *  { "transaction": { "merchant": "Habbib's", "amount": 90, "time": "2019-02-13T11:00:00.000Z" } }
   *  { "transaction": { "merchant": "Habbib's", "amount": 90, "time": "2019-02-13T11:01:00.000Z" } }
   */
  public static void main(String[] args)
  {
    File input;
    if (0 < args.length) {
      input = new File(args[0]);
      logger.info(String.format("File to process: %s", input));
      processFile(input);
    } else {
      logger.info("No input file provided, ergo, nothing to process");
    }
  }

  private static void processFile(File input)
  {
    try(BufferedReader reader = new BufferedReader(new FileReader(input))) {
      String line;
      String outputFile = Constants.OUT_FILE_NAME_PREFIX + System.currentTimeMillis();
      int totalCount = 0;
      logger.info(String.format("Reading from file %s", input));

      while ((line = reader.readLine()) != null) {
        String result = service.processLine(line);
        writeOutputFile(result, outputFile);
        totalCount++;
      }

      logger.info(String.format("%d lines processed, check out on file %s for details", totalCount, outputFile ));
    } catch (IOException e) {
      logger.error(String.format("Could not read from file %s %s", input, e.getMessage()));
    }
  }

  private static void writeOutputFile(String outputResponse, String outputFile ) {
      try(BufferedWriter writer = new BufferedWriter(new FileWriter("utils/" + outputFile, true))) {
        writer.append(outputResponse);
        writer.append("\n");
      } catch (IOException e) {
      logger.error(String.format("Could not write to file %s please make sure \"utils\" folder exists", e.getMessage()));
    }
  }
}
