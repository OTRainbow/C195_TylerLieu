package com.tylerlieu.c195.model;

public class Country {
    // From countries table
    private int countryID;
    private String name;
    // Constructor
    public Country(int countryID, String name) {
        this.countryID = countryID;
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
    public String getName() {
        return name;
    }
    public int getCountryID() {
        return countryID;
    }
}
