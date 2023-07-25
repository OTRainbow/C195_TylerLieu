package com.tylerlieu.c195.model;

public class Customer {
    private int customerID;
    private String name;
    private String phone;
    private String address;
    private int divisionID;
    private String divisionName;
    private int countryID;
    private String countryName;
    private String postal;
    private Division division;
    private Country country;

    // Full Customer Constructor
    public Customer(int customerID, String name, String phone, String address, int divisionID, String divisionName, int countryID, String countryName, String postal) {
        this.customerID = customerID;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
        this.countryName = countryName;
        this.postal = postal;
        this.division = new Division(divisionID, divisionName, countryID);
        this.country = new Country(countryID, countryName);
    }

    // Constructor overload for adding customer
    public Customer(String name, String address, String postal, String phone, int divisionID) {
        this.name = name;
        this.address = address;
        this.postal = postal;
        this.phone = phone;
        this.divisionID = divisionID;
    }
    // Constructor overload for update customer
    public Customer(int customerID, String name, String address, String postal, String phone, int divisionID) {
        this.customerID = customerID;
        this.name = name;
        this.address = address;
        this.postal = postal;
        this.phone = phone;
        this.divisionID = divisionID;
    }
    // Constructor overload for appointment
    public Customer(int customerID, String name) {
        this.customerID = customerID;
        this.name = name;
    }
    public int getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public int getCountryID() {
        return countryID;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getPostal() {
        return postal;
    }

    public Division getDivision() {
        return division;
    }

    public Country getCountry() {
        return country;
    }
}
