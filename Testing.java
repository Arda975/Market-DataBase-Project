import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Testing {

  public static long getMin(long arr[], int n) {
    long result = arr[0];
    for (int i = 1; i < n; i++)
      result = Math.min(result, arr[i]);
    return result;
  }

  public static long getMax(long arr[], int n) {
    long result = arr[0];
    for (int i = 1; i < n; i++)
      result = Math.max(result, arr[i]);
    return result;
  }

  public static long getAvg(long arr[], int n) {

    long result = 0;
    for (int i = 0; i < n; i++) {
      result = result + arr[i];
    }
    result = result / n;
    return result;
  }

  public static void main(String[] args) throws IOException {

    HashedDictionary<String, List<String>> dataBase = new HashedDictionary<>();

    String csvFile = "supermarket_dataset_50K.csv";
    long indexing_time1 = System.nanoTime();
    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
      String line;
      while ((line = br.readLine()) != null) {
        // Split the line by comma
        String[] values = line.split(",");
        String customerID = values[0].trim();
        String customerName = values[1].trim();
        String date = values[2].trim();
        String productName = values[3].trim();

        // Check if the customer ID is already in the database
        if (dataBase.contains(customerID)) {
          // If the customer exists, retrieve their transactions and add the new one
          List<String> transactions = dataBase.getValue(customerID);
          transactions.add(date + ", " + productName);
        } else {
          // If the customer doesn't exist, create a new list and add the transaction
          List<String> transactions = new ArrayList<>();
          transactions.add(date + ", " + productName);
          transactions.add(0, customerName);
          dataBase.add(customerID, transactions);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    long indexing_time2 = System.nanoTime();
    System.out.println("Indexing time is: " + (indexing_time2 - indexing_time1));

    // String searchCustomerID = "25666524-43df-4fac-8650-108deea6e94a";
    // if (dataBase.contains(searchCustomerID)) {
    // List<String> transactions = dataBase.getValue(searchCustomerID);
    // System.out.println(">Search: " + searchCustomerID);
    // System.out.println(transactions.size() - 1 + " transactions found for " +
    // transactions.get(0));

    // String temp = transactions.remove(0);
    // // Sort the transactions based on their dates
    // Collections.sort(transactions, (t1, t2) -> {
    // String date1 = t1.split(",")[0].trim();
    // String date2 = t2.split(",")[0].trim();
    // return date2.compareTo(date1); // Compare dates in reverse order
    // });
    // for (String transaction : transactions) {
    // System.out.println(transaction);
    // }
    // transactions.add(0, temp);
    // } else {
    // System.out.println("Customer not found!");
    // }

    String searchFile = "customer_1K.txt";
    long[] search = new long[1000];
    int a = 0;
    try (BufferedReader br = new BufferedReader(new FileReader(searchFile))) {
      String searchKey;
      while ((searchKey = br.readLine()) != null) {
        if (dataBase.contains(searchKey)) {
          long indexing_time3 = System.nanoTime();
          List<String> transactions = dataBase.getValue(searchKey);
          long indexing_time4 = System.nanoTime();
          search[a] = indexing_time4 - indexing_time3;
          a++;
          // System.out.println(">Search: " + searchKey);
          // System.out.println(transactions.size() - 1 + " transactions found for " +
          // transactions.get(0));

          String temp = transactions.remove(0);
          Collections.sort(transactions, (t1, t2) -> {
            String date1 = t1.split(",")[0].trim();
            String date2 = t2.split(",")[0].trim();
            return date2.compareTo(date1);
          });
          for (String transaction : transactions) {
            // System.out.println(transaction);
          }
          transactions.add(0, temp);
        } else {
          // System.out.println("Customer not found: " + searchKey);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println();
    System.out.println(
        "Average search time is :" + getAvg(search, 1000) + " \nMinimum search time is : " + getMin(search, 1000)
            + " \nMaximum search time is :"
            + getMax(search, 1000));
    System.out.println("Collision Count is :" + dataBase.collisionCount);

  }

}
