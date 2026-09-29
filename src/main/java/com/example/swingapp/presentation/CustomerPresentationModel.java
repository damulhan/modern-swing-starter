package com.example.swingapp.presentation;

import com.example.swingapp.model.Customer;
import com.example.swingapp.service.CustomerService;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueModel;

import java.util.List;

public class CustomerPresentationModel {
    private final CustomerService customerService;
    private final PresentationModel<Customer> presentationModel;
    private Customer currentCustomer;

    private final ValueModel nameModel;
    private final ValueModel emailModel;
    private final ValueModel phoneModel;
    private final ValueModel activeModel;

    public CustomerPresentationModel(CustomerService customerService) {
        this.customerService = customerService;
        this.currentCustomer = new Customer();
        this.presentationModel = new PresentationModel<>(currentCustomer);

        // Bind buffered / direct models to Customer properties
        this.nameModel = presentationModel.getModel(Customer.PROPERTY_NAME);
        this.emailModel = presentationModel.getModel(Customer.PROPERTY_EMAIL);
        this.phoneModel = presentationModel.getModel(Customer.PROPERTY_PHONE);
        this.activeModel = presentationModel.getModel(Customer.PROPERTY_ACTIVE);
    }

    public ValueModel getNameModel() {
        return nameModel;
    }

    public ValueModel getEmailModel() {
        return emailModel;
    }

    public ValueModel getPhoneModel() {
        return phoneModel;
    }

    public ValueModel getActiveModel() {
        return activeModel;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCustomer(Customer customer) {
        this.currentCustomer = (customer != null) ? customer.copy() : new Customer();
        this.presentationModel.setBean(this.currentCustomer);
    }

    public void createNewCustomer() {
        setCustomer(new Customer(null, "", "", "", true));
    }

    public String validateCurrent() {
        String name = (String) nameModel.getValue();
        if (name == null || name.trim().isEmpty()) {
            return "고객 이름을 입력해 주세요.";
        }
        return null;
    }

    public Customer saveCurrent() {
        Customer saved = customerService.save(this.currentCustomer);
        setCustomer(saved);
        return saved;
    }

    public boolean deleteCurrent() {
        if (currentCustomer.getId() != null) {
            boolean deleted = customerService.delete(currentCustomer.getId());
            if (deleted) {
                createNewCustomer();
            }
            return deleted;
        }
        return false;
    }

    public List<Customer> search(String keyword) {
        return customerService.search(keyword);
    }

    public List<Customer> getAllCustomers() {
        return customerService.findAll();
    }
}
