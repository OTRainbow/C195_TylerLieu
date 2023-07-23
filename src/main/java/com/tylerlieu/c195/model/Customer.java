package com.tylerlieu.c195.model;

public class Customer {
    // From customers table
    private int customerID;
    private String name;
    private String address;
    private String postal;
    private String phone;
    private int divisionID;

    // Derived from divisionID
    private Division division;
}
