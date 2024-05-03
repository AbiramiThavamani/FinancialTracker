package com.pluralsight;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);


    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String fileName) {

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\|"); // pipe
                if (parts.length == 5) {
                    LocalDate date = LocalDate.parse(parts[0].trim(), DATE_FORMATTER); // convert string to date
                    LocalTime time = LocalTime.parse(parts[1].trim(), TIME_FORMATTER);
                    String description = parts[2].trim();
                    String vendor = parts[3].trim();
                    double amount = Double.parseDouble(parts[4]);
                    transactions.add(new Transaction(date, time, description, vendor, amount)); // added in Arraylist
                }

            }
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
    }

    private static void addDeposit(Scanner scanner) {

        System.out.print("Enter the date (yyyy-MM-dd): "); // prompt the user for date
        LocalDate dateString = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
        System.out.print("Enter the time (HH:mm:ss): "); // prompt the user for time
        LocalTime timeString = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);
        System.out.println("Enter the description: ");
        String description = scanner.nextLine();
        System.out.print("Enter the vendor name: ");
        String vendor = scanner.nextLine(); // prompt the user of vendor
        System.out.println("Enter the deposit amount: "); // positive amount
        double depositAmount = Double.parseDouble(scanner.nextLine());
        if (depositAmount <= 0) {
            System.out.println("Invalid amount please try again. ");
            return; // exit the method or handle the error appropriately
        }

        // crete new deposit object  transaction
        Transaction deposit = new Transaction(dateString, timeString, description, vendor, depositAmount);
        transactions.add(deposit); // added to the ArrayList

        System.out.println("The deposit added successfully");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            writer.write(dateString + "|" + timeString + "|" + description + "|" + vendor + "|" + depositAmount + "\n");
            System.out.println("Deposit added successfully to the transactions.csv file: ");
            writer.close();
        } catch (Exception e) {
            System.out.println("An error occurred while closing the FileWriter: " + e.getMessage());
        }

    }

    private static void addPayment(Scanner scanner) {

        System.out.print("Enter the date (yyyy-MM-dd): "); // prompt the user for date
        LocalDate dateString = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
        System.out.print("Enter the time (HH:mm:ss): "); // prompt the user for time
        LocalTime timeString = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);
        System.out.println("Enter the description: ");
        String description = scanner.nextLine();
        System.out.print("Enter the vendor name: ");
        String vendor = scanner.nextLine(); // prompt the user of vendor
        System.out.println("Enter the amount of a payment: ");
        double amountPayment = Double.parseDouble(scanner.nextLine());
        if (amountPayment <= 0) {
            System.out.println("Invalid amount please try again ");
            return;
        }
        amountPayment *= -1;

        // create new  payment object
        Transaction payment = new Transaction(dateString, timeString, description, vendor, amountPayment);
        transactions.add(payment);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            writer.write(dateString + "|" + timeString + "|" + description + "|" + vendor + "|" + amountPayment + "\n");
            System.out.println("Payment added successfully to the transaction csv file");
            writer.close();
        } catch (Exception e) {
            System.out.println("An error occurred while closing the FileWriter: " + e.getMessage());
        }
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
       // all transaction list display
        System.out.println("type in all list");
        for (Transaction myTransaction : transactions) {
            System.out.println(myTransaction.getDate() + "|" + myTransaction.getTime() + "|" + myTransaction.getType() + "|" + myTransaction.getVendor() + "|" + myTransaction.getPayment());
        }

    }

    private static void displayDeposits() {
       // all deposit transaction list
        System.out.println("deposit in all list");
        for (Transaction myTransaction : transactions) {
            if (myTransaction.getPayment() >= 0) {
                System.out.println(myTransaction.getDate() + "|" + myTransaction.getTime() + "|" + myTransaction.getType() + "|" + myTransaction.getVendor() + "|" + myTransaction.getPayment());
            }
        }
    }


    private static void displayPayments() {

        System.out.println("payment in all list");
        for (Transaction myTransaction : transactions) {
            if (myTransaction.getPayment() <= 0) {
                System.out.println(myTransaction.getDate() + "|" + myTransaction.getTime() + "|" + myTransaction.getType() + "|" + myTransaction.getVendor() + "|" + myTransaction.getPayment());

            }
        }
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    // Generate a report for all transactions within the current month,
                    // including the date, vendor, and amount for each transaction.

                    LocalDate currentMonth = LocalDate.now();
                    System.out.println("displaying all transactions for the month of " + currentMonth.getMonth());
                    filterTransactionsByDate(currentMonth.withDayOfMonth(1), currentMonth);
                    break;


                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, vendor, and amount for each transaction.

                    LocalDate previousMonth = LocalDate.now().minusMonths(1);
                    System.out.println("displaying all transaction for the month of " + previousMonth.getMonth());
                    filterTransactionsByDate(previousMonth.withDayOfMonth(1), previousMonth.withDayOfMonth(previousMonth.lengthOfMonth()));
                    break;


                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate currentYear = LocalDate.now();
                    System.out.println("displaying all transaction for the year of " + currentYear.getYear());
                    filterTransactionsByDate(currentYear.withDayOfYear(1), currentYear);
                    break;

                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate previousYear = LocalDate.now().minusYears(1);
                    System.out.println("displaying all transactions for the year of " + previousYear.getYear());
                    filterTransactionsByDate(previousYear.withDayOfYear(1), previousYear.withMonth(12).withDayOfMonth(31));
                    break;
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, vendor, and amount for each transaction.
                    System.out.println("Enter a vendor name: ");
                    String vendorName = scanner.nextLine().trim();
                    filterTransactionsByVendor(vendorName);
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {

        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
        ArrayList<Transaction> found = new ArrayList<>();
        System.out.println("Transaction with a date range: ");
        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (transactionDate.isAfter(startDate.minusDays(1)) && transactionDate.isBefore(endDate.plusDays(1))) {
                found.add(transaction);
                System.out.println(transaction);

            }
        }
        if (found.isEmpty()) {
            System.out.println("no transactions found within the given date range!. ");
        }

    }


    private static void filterTransactionsByVendor(String vendor) {

        
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
        ArrayList<Transaction> found = new ArrayList<>();
        System.out.println("Transaction for vendor: " + vendor);
        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                found.add(transaction);
                System.out.println(transaction);
            }
        }
        if (found.isEmpty()) {
            System.out.println("no transaction found with given vendor!: ");
        }
    }
}
  /*  private static void filterTransactionsByCustomSearch(Scanner scanner){
        System.out.print("Enter the date (yyyy-MM-dd): "); // prompt the user for date
        LocalDate dateString = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
        System.out.print("Enter the time (HH:mm:ss): "); // prompt the user for time
        LocalTime timeString = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);
        System.out.println("Enter the description: ");
        String description = scanner.nextLine();
        System.out.print("Enter the vendor name: ");
        String vendor = scanner.nextLine(); // prompt the user of vendor
        System.out.println("Enter the amount of a payment: ");
        double amountPayment = Double.parseDouble(scanner.nextLine());
        if (amountPayment == 0.0) {
            System.out.println("Invalid amount please try again ");
            return;
        }
        amountPayment *= -1;

        ArrayList<Transaction> found = new ArrayList<>();
        System.out.println("Transaction for Custom Search: " );
        for (Transaction transaction : transactions){
            LocalDate transactionDate = transaction.getDate();
            if (transactionDate.isAfter(startDate.minusDays(1))
                    && transactionDate.isBefore(endDate.plusDays(1))
                    && transaction.getType().contains(type)
                    && transaction.getVendor().equalsIgnoreCase(vendor)
                    && transaction.getPayment()==amount) {
                found.add(transaction);
            }
        }
        if (found.isEmpty()){
            System.out.println("no transaction found with given Custom Search");

        }
    }
*/