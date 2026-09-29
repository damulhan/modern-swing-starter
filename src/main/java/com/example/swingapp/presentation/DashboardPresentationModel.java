package com.example.swingapp.presentation;

import com.example.swingapp.model.Order;
import com.example.swingapp.service.CustomerService;

import java.util.List;

public class DashboardPresentationModel {
    private final CustomerService customerService;

    public DashboardPresentationModel(CustomerService customerService) {
        this.customerService = customerService;
    }

    public int getCustomerCount() {
        return customerService.getCustomerCount();
    }

    public int getOrderCount() {
        return customerService.getOrderCount();
    }

    public double getTotalRevenue() {
        return customerService.getTotalRevenue();
    }

    public List<Order> getRecentOrders() {
        return customerService.getRecentOrders();
    }
}
