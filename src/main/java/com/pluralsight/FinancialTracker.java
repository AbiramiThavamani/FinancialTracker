package com.pluralsight;

import jdk.jfr.Description;

import javax.imageio.IIOException;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    Scanner scanner = new Scanner(System.in);

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
        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>,<time>,<vendor>,<type>,<amount>
        // For example: 2023-04-29,13:45:00,Amazon,PAYMENT,29.99
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    LocalDate date = LocalDate.parse(parts[0].trim());
                    LocalTime time = LocalTime.parse(parts[1].trim());
                    String type = parts[2].trim();
                    String vendor = parts[3].trim();
                    double amount = Double.parseDouble(parts[4]);
                    transactions.add(new Transaction(date, time, type, vendor, amount));
                }

            }
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
    }

    private static void addDeposit(Scanner scanner) {
        // This method should prompt the user to enter the date, time, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Deposit` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.
        System.out.print("Enter the date (yyyy-MM-dd): "); // prompt the user for date
        LocalDate dateString = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
        System.out.print("Enter the time (HH:mm:ss): "); // prompt the user for time
        LocalTime timeString = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);
        System.out.print("Enter the vendor name: ");
        String vendor = scanner.nextLine(); // prompt the user of vendor
        System.out.println("Enter the deposit amount: ");
        double depositAmount = 0;
        try {
            depositAmount = Double.parseDouble(scanner.nextLine());
            if (depositAmount <= 0) {
                System.out.println("Deposit amount must be positive number. ");
                return; // exit the method or handle the error appropriately
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format. please enter a valid number. ");
            return;  // exit the method or handle the error appropriately
        }


        // crete new deposit transaction
        Transaction deposit = new Transaction(dateString, timeString, "deposit", vendor, depositAmount);
        transactions.add(deposit);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(FILE_NAME, true);
            fileWriter.write(dateString + "|" + timeString + "|deposit|" + vendor + "|" + depositAmount + "\n");
        } catch (Exception e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (Exception e) {
                System.out.println("An error occurred while closing the FileWriter: " + e.getMessage());
            }
        }

    }


    private static void addPayment(Scanner scanner) {
        // This method should prompt the user to enter the date, time, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Payment` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.
        System.out.print("Enter the date (yyyy-MM-dd): "); // prompt the user for date
        LocalDate dateString = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
        System.out.print("Enter the time (HH:mm:ss): "); // prompt the user for time
        LocalTime timeString = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);
        System.out.print("Enter the vendor name: ");
        String vendor = scanner.nextLine(); // prompt the user of vendor
        System.out.println("Enter the amount of a payment: ");
        double amountPayment = -1;
        while (amountPayment < 0) {
            System.out.println("Enter the amount positive number: ");
            try {
                amountPayment = Double.parseDouble(scanner.nextLine());
                if (amountPayment < 0) {
                    System.out.println("Amount must be a positive number: ");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input Amount must be a number. ");
            }
        }

        // create new  payment object
        Transaction payment = new Transaction(dateString, timeString, "payment", vendor, amountPayment);
        transactions.add(payment);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(FILE_NAME, true);
            fileWriter.write(dateString + "|" + timeString + "|deposit|" + vendor + "|" + amountPayment + "\n");
        } catch (Exception e) {
            System.out.println("payment added successfully: " + e.getMessage());
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (Exception e) {
                System.out.println("An error occurred while closing the FileWriter: " + e.getMessage());
            }
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
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, type, and amount.
        for(int i = 0; i<transactions.size(); i++){
            Transaction myTransaction = transactions.get(i);
            System.out.println(myTransaction.getDate() + "|" + myTransaction.getTime() + "|"+ myTransaction.getType() + "|" + myTransaction.getVendor() + "|" + myTransaction.getPayment());
        }

    }

    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, and amount.
        for (int i = 0; i < transactions.size(); i++) {
            Transaction myTransaction = transactions.get(i);
            String a = myTransaction.getType();;
            if(a.equals("deposit")) {
                System.out.println(myTransaction.getDate() + "|" + myTransaction.getTime() + "|"+ myTransaction.getType() + "|" + myTransaction.getVendor() + "|" + myTransaction.getPayment());
            }
        }
    }

    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, and amount.
        for (int i = 0; i < transactions.size(); i++) {
            Transaction myTransaction = transactions.get(i);
            String a = myTransaction.getType();
            if(a.equals("payment")) {
                System.out.println(myTransaction.getDate() + "|" + myTransaction.getTime() + "|"+ myTransaction.getType() + "|" + myTransaction.getVendor() + "|" + myTransaction.getPayment());
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
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, vendor, and amount for each transaction.
                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, vendor, and amount for each transaction.

                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, vendor, and amount for each transaction.
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, vendor, and amount for each transaction.
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
    }

    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
    }
}