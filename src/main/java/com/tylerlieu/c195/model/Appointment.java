package com.tylerlieu.c195.model;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an Appointment record from the appointments table
 */
public class Appointment {
    /**
     * The Primary Key
     */
    private int appointmentID;
    /**
     * This Appointment's title
     */
    private String title;
    /**
     * This Appointment's description
     */
    private String description;
    /**
     * This Appointment's location
     */
    private String location;
    /**
     * This Appointment's type
     */
    private String type;
    /**
     * The Foreign Key for this Appointment's Customer
     */
    private int customerID;
    /**
     * The name of this Appointment's Customer
     */
    private String customerName;
    /**
     * The Foreign Key for this Appointment's User
     */
    private int userID;
    /**
     * The name of this Appointment's User
     */
    private String userName;
    /**
     * The Foreign Key for this Appointment's Contact
     */
    private int contactID;
    /**
     * The name of this Appointment's Contact
     */
    private String contactName;
    /**
     * The email address of this Appointment's Contact
     */
    private String contactEmail;
    /**
     * The Customer assigned to this Appointment
     *
     * DERIVED from customerID and customerName
     */
    private Customer customer;
    /**
     * The User assigned to this Appointment
     *
     * DERIVED from userID and userName
     */
    private User user;
    /**
     * The Contact assigned to this Appointment
     *
     * DERIVED from contactID, contactName, and contactEmail
     */
    private Contact contact;
    /**
     * The start date/time in UTC
     *
     * DERIVED from startDate
     */
    private ZonedDateTime startDateUTC;
    /**
     * The end date/time in UTC
     *
     * DERIVED from endDate
     */
    private ZonedDateTime endDateUTC;
    /**
     * The local start date/time
     *
     * DERIVED from startDate
     */
    private ZonedDateTime startDateLocal;
    /**
     * The local end date/time
     *
     * DERIVED from endDate
     */
    private ZonedDateTime endDateLocal;
    /**
     * A formated local start date/time
     *
     * DERIVED from startDateLocal
     */
    private String startDateLocalDisplay;
    /**
     * A formated local end date/time
     *
     * DERIVED from endDateLocal
     */
    private String endDateLocalDisplay;

    /**
     * Fully constructs an Appointment object with all fields populated.
     * Used when calling AppointmentsQuery.getAllAppointmentRecords().
     * @param appointmentID Primary Key
     * @param title Appointment title
     * @param description Appointment description
     * @param location Appointment location
     * @param type Appointment type
     * @param startDate Appointment start
     * @param endDate Appointment end
     * @param customerID Foreign Key for Customer
     * @param customerName Customer name
     * @param userID Foreign Key for User
     * @param userName User name
     * @param contactID Foreign Key for Contact
     * @param contactName Contact name
     * @param contactEmail Contact email
     */
    public Appointment(int appointmentID, String title, String description, String location, String type, ZonedDateTime startDate, ZonedDateTime endDate, int customerID, String customerName, int userID, String userName, int contactID, String contactName, String contactEmail) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
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
        this.startDateLocalDisplay = startDateLocal.toLocalDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mma"));
        this.endDateLocalDisplay = endDateLocal.toLocalDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mma"));
    }

    /**
     * Constructs an Appointment object without the appointmentID.
     * Used for adding an Appointment to the database.
     * @param title Appointment title
     * @param description Appointment description
     * @param location Appointment location
     * @param type Appointment type
     * @param startDate Appointment start
     * @param endDate Appointment end
     * @param customerID Foreign Key for Customer
     * @param customerName Customer name
     * @param userID Foreign Key for User
     * @param userName User name
     * @param contactID Foreign Key for Contact
     * @param contactName Contact name
     * @param contactEmail Contact email
     */
    public Appointment(String title, String description, String location, String type, ZonedDateTime startDate, ZonedDateTime endDate, int customerID, String customerName, int userID, String userName, int contactID, String contactName, String contactEmail) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
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
        this.startDateLocalDisplay = startDateLocal.toLocalDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mma"));
        this.endDateLocalDisplay = endDateLocal.toLocalDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mma"));
    }

    /**
     * Returns this Appointment's appointmentID
     * @return Primary Key
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * Returns this Appointment's title
     * @return Appointment's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns this Appointment's description
     * @return Appointment's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns this Appointment's location
     * @return Appointment's location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns this Appointment's type
     * @return Appointment's type
     */
    public String getType() {
        return type;
    }

    /**
     * Returns this Appointment's Customer's customerID
     * @return Foreign Key for Customer
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Returns this Appointment's Customer's customerName
     * @return Customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Returns this Appointment's User's userID
     * @return Foreign Key for User
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Returns this Appointment's User's userName
     * @return User name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Returns this Appointment's Contact's contactID
     * @return Foreign Key for Contact
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Returns this Appointment's Contact's contactName
     * @return Contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Returns this Appointment's Contact's contactEmail
     * @return Contact email
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * Returns this Appointment's Customer
     * @return Customer object
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Returns this Appointment's User
     * @return User object
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns this Appointment's Contact
     * @return Contact object
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Returns this Appointment's startDateUTC
     * @return Appointment's startDateUTC
     */
    public ZonedDateTime getStartDateUTC() {
        return startDateUTC;
    }

    /**
     * Returns this Appointment's endDateUTC
     * @return Appointment's endDateUTC
     */
    public ZonedDateTime getEndDateUTC() {
        return endDateUTC;
    }

    /**
     * Returns this Appointment's startDateLocal
     * @return Appointment's startDateLocal
     */
    public ZonedDateTime getStartDateLocal() {
        return startDateLocal;
    }

    /**
     * Returns this Appointment's endDateLocal
     * @return Appointment's endDateLocal
     */
    public ZonedDateTime getEndDateLocal() {
        return endDateLocal;
    }

    /**
     * Returns this Appointment's startDateLocalDisplay
     * @return Appointment's startDateLocalDisplay
     */
    public String getStartDateLocalDisplay() {
        return startDateLocalDisplay;
    }

    /**
     * Returns this Appointment's endDateLocatDisplay
     * @return Appointment's endDateLocalDisplay
     */
    public String getEndDateLocalDisplay() {
        return endDateLocalDisplay;
    }
}
