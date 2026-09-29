package com.example.swingapp.model;

import java.time.LocalDate;

public class Order {
    private String id;
    private String customerName;
    private String status;
    private double amount;
    private LocalDate date;

    public Order(String id, String customerName, String status, double amount, LocalDate date) {
        this.id = id;
        this.customerName = customerName;
        this.status = status;
        this.amount = amount;
        this.date = date;
    }

    public String getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getStatus() { return status; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
}
