package com.example.swingapp.service;

import com.example.swingapp.model.Customer;
import com.example.swingapp.model.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerService {
    private final List<Customer> customers = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public CustomerService() {
        initSampleData();
    }

    private void initSampleData() {
        save(new Customer(null, "John Doe", "john.doe@example.com", "010-1234-5678", true));
        save(new Customer(null, "Alice Smith", "alice.smith@example.com", "010-2345-6789", true));
        save(new Customer(null, "Bob Johnson", "bob.j@example.com", "010-3456-7890", false));
        save(new Customer(null, "Emma Watson", "emma.w@example.com", "010-4567-8901", true));
        save(new Customer(null, "Michael Brown", "mbrown@example.com", "010-5678-9012", true));
        save(new Customer(null, "Sarah Davis", "sarah.d@example.com", "010-6789-0123", false));
        save(new Customer(null, "James Wilson", "jwilson@example.com", "010-7890-1234", true));

        orders.add(new Order("ORD-1001", "John Doe", "Completed", 1250.00, LocalDate.now().minusDays(1)));
        orders.add(new Order("ORD-1002", "Alice Smith", "Processing", 340.50, LocalDate.now().minusDays(2)));
        orders.add(new Order("ORD-1003", "Emma Watson", "Completed", 4520.00, LocalDate.now().minusDays(3)));
        orders.add(new Order("ORD-1004", "Michael Brown", "Shipped", 890.00, LocalDate.now().minusDays(4)));
        orders.add(new Order("ORD-1005", "James Wilson", "Completed", 2150.00, LocalDate.now().minusDays(5)));
    }

    public synchronized List<Customer> findAll() {
        List<Customer> copy = new ArrayList<>();
        for (Customer c : customers) {
            copy.add(c.copy());
        }
        return copy;
    }

    public synchronized List<Customer> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        String kw = keyword.trim().toLowerCase();
        List<Customer> list = new ArrayList<>();
        for (Customer c : customers) {
            if (c.getName().toLowerCase().contains(kw) ||
                c.getEmail().toLowerCase().contains(kw) ||
                c.getPhone().contains(kw)) {
                list.add(c.copy());
            }
        }
        return list;
    }

    public synchronized Customer save(Customer customer) {
        if (customer.getId() == null) {
            customer.setId(idGenerator.getAndIncrement());
            customers.add(customer.copy());
            return customer;
        } else {
            Optional<Customer> existing = customers.stream()
                    .filter(c -> c.getId().equals(customer.getId()))
                    .findFirst();
            if (existing.isPresent()) {
                Customer c = existing.get();
                c.setName(customer.getName());
                c.setEmail(customer.getEmail());
                c.setPhone(customer.getPhone());
                c.setActive(customer.isActive());
                return c.copy();
            } else {
                customers.add(customer.copy());
                return customer;
            }
        }
    }

    public synchronized boolean delete(Long id) {
        return customers.removeIf(c -> c.getId().equals(id));
    }

    public synchronized List<Order> getRecentOrders() {
        return new ArrayList<>(orders);
    }

    public synchronized int getCustomerCount() {
        return customers.size();
    }

    public synchronized int getOrderCount() {
        return orders.size();
    }

    public synchronized double getTotalRevenue() {
        return orders.stream().mapToDouble(Order::getAmount).sum();
    }
}
