package com.tylerlieu.c195.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment {
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
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
    // Derived from startDate and endDate
    private ZonedDateTime startDateUTC;
    private ZonedDateTime endDateUTC;
    private ZonedDateTime startDateLocal;
    private ZonedDateTime endDateLocal;
    private ZonedDateTime startDateInET;
    private ZonedDateTime endDateInET;
    private String startDateLocalDisplay;
    private String endDateLocalDisplay;
    public Appointment(int appointmentID, String title, String description, String location, String type, ZonedDateTime startDate, ZonedDateTime endDate, int customerID, String customerName, int userID, String userName, int contactID, String contactName, String contactEmail) {
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

        if (startDate.getZone().equals(ZoneOffset.UTC)) {
            this.startDateUTC = startDate;
        } else {
            this.startDateUTC = startDate.withZoneSameInstant(ZoneOffset.UTC);
        }
        if (endDate.getZone().equals(ZoneOffset.UTC)) {
            this.endDateUTC = endDate;
        } else {
            this.endDateUTC = endDate.withZoneSameInstant(ZoneOffset.UTC);
        }
        if (startDate.getZone().equals(ZoneId.systemDefault())) {
            this.startDateLocal = startDate;
        } else {
            this.startDateLocal = startDate.withZoneSameInstant(ZoneId.systemDefault());
        }
        if (endDate.getZone().equals(ZoneId.systemDefault())) {
            this.endDateLocal = endDate;
        } else {
            this.endDateLocal = endDate.withZoneSameInstant(ZoneId.systemDefault());
        }
        if (startDate.getZone().equals(ZoneId.of("America/New_York"))) {
            this.startDateInET = startDate;
        } else {
            this.startDateInET = startDate.withZoneSameInstant(ZoneId.of("America/New_York"));
        }
        if (endDate.getZone().equals(ZoneId.of("America/New_York"))) {
            this.endDateInET = endDate;
        } else {
            this.endDateInET = endDate.withZoneSameInstant(ZoneId.of("America/New_York"));
        }
        this.startDateLocalDisplay = startDateLocal.toLocalDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mma"));
        this.endDateLocalDisplay = endDateLocal.toLocalDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mma"));
    }
    public Appointment(String title, String description, String location, String type, ZonedDateTime startDate, ZonedDateTime endDate, int customerID, String customerName, int userID, String userName, int contactID, String contactName, String contactEmail) {
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

        if (startDate.getZone().equals(ZoneOffset.UTC)) {
            this.startDateUTC = startDate;
        } else {
            this.startDateUTC = startDate.withZoneSameInstant(ZoneOffset.UTC);
        }
        if (endDate.getZone().equals(ZoneOffset.UTC)) {
            this.endDateUTC = endDate;
        } else {
            this.endDateUTC = endDate.withZoneSameInstant(ZoneOffset.UTC);
        }
        if (startDate.getZone().equals(ZoneId.systemDefault())) {
            this.startDateLocal = startDate;
        } else {
            this.startDateLocal = startDate.withZoneSameInstant(ZoneId.systemDefault());
        }
        if (endDate.getZone().equals(ZoneId.systemDefault())) {
            this.endDateLocal = endDate;
        } else {
            this.endDateLocal = endDate.withZoneSameInstant(ZoneId.systemDefault());
        }
        if (startDate.getZone().equals(ZoneId.of("America/New_York"))) {
            this.startDateInET = startDate;
        } else {
            this.startDateInET = startDate.withZoneSameInstant(ZoneId.of("America/New_York"));
        }
        if (endDate.getZone().equals(ZoneId.of("America/New_York"))) {
            this.endDateInET = endDate;
        } else {
            this.endDateInET = endDate.withZoneSameInstant(ZoneId.of("America/New_York"));
        }
        this.startDateLocalDisplay = startDateLocal.toLocalDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mma"));
        this.endDateLocalDisplay = endDateLocal.toLocalDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mma"));
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

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public ZonedDateTime getEndDate() {
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

    public ZonedDateTime getStartDateUTC() {
        return startDateUTC;
    }

    public ZonedDateTime getEndDateUTC() {
        return endDateUTC;
    }

    public ZonedDateTime getStartDateLocal() {
        return startDateLocal;
    }

    public ZonedDateTime getEndDateLocal() {
        return endDateLocal;
    }

    public ZonedDateTime getStartDateInET() {
        return startDateInET;
    }

    public ZonedDateTime getEndDateInET() {
        return endDateInET;
    }

    public String getStartDateLocalDisplay() {
        return startDateLocalDisplay;
    }

    public String getEndDateLocalDisplay() {
        return endDateLocalDisplay;
    }
}
