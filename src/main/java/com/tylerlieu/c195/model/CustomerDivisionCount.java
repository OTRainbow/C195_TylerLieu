package com.tylerlieu.c195.model;

/**
 * Represents a record that shows the number of customers from each division.
 * Specifically used for one of the three report tables.
 * Simplified to be easy to display in a TableView.
 */
public class CustomerDivisionCount {
    /**
     * Name of this Country
     */
    private final String countryName;
    /**
     * Name of this Division
     */
    private final String divisionName;
    /**
     * Total number of customers living in this division
     */
    private final int total;

    /**
     * Constructs a CustomerDivisionCount record
     * @param countryName Name of this Country
     * @param divisionName Name of this Division
     * @param total Total number of customers
     */
    public CustomerDivisionCount(String countryName, String divisionName, int total) {
        this.countryName = countryName;
        this.divisionName = divisionName;
        this.total = total;
    }

    /**
     * Returns this CustomerDivisionCount's countryName
     * @return countryName
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Returns this CustomerDivisionCount's divisionName
     * @return divisionName
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * Returns this CustomerDivisionCount's total
     * @return total
     */
    public int getTotal() {
        return total;
    }
}
