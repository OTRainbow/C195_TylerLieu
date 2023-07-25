package com.tylerlieu.c195.model;

public class Division {
    private int divisionID;
    private String name;
    private int countryID;
    public Division(int divisionID, String name, int countryID) {
        this.divisionID = divisionID;
        this.name = name;
        this.countryID = countryID;
    }
    @Override
    public String toString() {
        return name;
    }
    public String getName() {
        return name;
    }
    public int getDivisionID() {
        return divisionID;
    }
    public int getCountryID() {
        return countryID;
    }
}
