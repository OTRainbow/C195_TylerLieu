package com.tylerlieu.c195.model;

public class CustomerDivisionCount {
    private String countryName;
    private String divisionName;
    private int total;
    public CustomerDivisionCount(String countryName, String divisionName, int total) {
        this.countryName = countryName;
        this.divisionName = divisionName;
        this.total = total;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public int getTotal() {
        return total;
    }
}
