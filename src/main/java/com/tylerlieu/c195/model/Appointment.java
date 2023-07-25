package com.tylerlieu.c195.model;

import java.util.Date;

public class Appointment {
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private Date startDate;
    private Date endDate;
    private int customerID;
    private String customerName;
    private int userID;
    private String userName;
    private int contactID;
    private String contactName;
    private String contactEmail;
    private Customer customer;
    private User user;
    private Contact contact;
    public Appointment(int appointmentID, String title, String description, String location, String type, Date startDate, Date endDate, int customerID, String customerName, int userID, String userName, int contactID, String contactName, String contactEmail) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customerID = customerID;
        this.customerName = customerName;
        this.userID = userID;
        this.userName = userName;
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.customer = new Customer(customerID, customerName);
        this.user = new User(userID, userName);
        this.contact = new Contact(contactID, contactName, contactEmail);
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public int getContactID() {
        return contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public Customer getCustomer() {
        return customer;
    }

    public User getUser() {
        return user;
    }

    public Contact getContact() {
        return contact;
    }
}
