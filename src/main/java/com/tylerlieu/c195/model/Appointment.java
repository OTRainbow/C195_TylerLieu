package com.tylerlieu.c195.model;

import java.util.Date;

public class Appointment {
    // From appointments table
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private Date startDate;
    private Date endDate;
    private int customerID;
    private int userID;
    private int contactID;

    // Derived from customerID
    private Customer customer;
    // Derived from userID;
    private User user;
    // Derived from contactID;
    private Contact contact;
}
