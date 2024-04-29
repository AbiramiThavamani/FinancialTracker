package com.pluralsight;

public class Transaction {
    private String date;
    private String time;
    private String vendor;

    private String type;
    private double amount;

    public Transaction(String date, String time, String vendor, String type, double amount) {
        this.date = date;
        this.time = time;
        this.vendor = vendor;
        this.type = type;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getVendor() {
        return vendor;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}
