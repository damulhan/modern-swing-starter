package com.example.swingapp.model;

import com.jgoodies.binding.beans.Model;

public class Customer extends Model {
    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_EMAIL = "email";
    public static final String PROPERTY_PHONE = "phone";
    public static final String PROPERTY_ACTIVE = "active";

    private Long id;
    private String name;
    private String email;
    private String phone;
    private boolean active;

    public Customer() {
        this(null, "", "", "", true);
    }

    public Customer(Long id, String name, String email, String phone, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        Long oldValue = this.id;
        this.id = id;
        firePropertyChange(PROPERTY_ID, oldValue, id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldValue = this.name;
        this.name = name;
        firePropertyChange(PROPERTY_NAME, oldValue, name);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String oldValue = this.email;
        this.email = email;
        firePropertyChange(PROPERTY_EMAIL, oldValue, email);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        String oldValue = this.phone;
        this.phone = phone;
        firePropertyChange(PROPERTY_PHONE, oldValue, phone);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        boolean oldValue = this.active;
        this.active = active;
        firePropertyChange(PROPERTY_ACTIVE, oldValue, active);
    }

    public Customer copy() {
        return new Customer(this.id, this.name, this.email, this.phone, this.active);
    }
}
