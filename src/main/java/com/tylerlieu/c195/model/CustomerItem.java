package com.tylerlieu.c195.model;

public class CustomerItem {
    private int customerID;
    private String name;
    private String phone;
    private String address;
    private String division;
    private String country;
    private String postal;

    public CustomerItem(int customerID, String name, String phone, String address, String division, String country, String postal) {
        this.customerID = customerID;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.division = division;
        this.country = country;
        this.postal = postal;
    }
    // ----------------------------------------------------
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

    public String getDivision() {
        return division;
    }

    public String getCountry() {
        return country;
    }

    public String getPostal() {
        return postal;
    }
    // ----------------------------------------------------
    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }
}
