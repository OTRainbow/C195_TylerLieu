package com.tylerlieu.c195.model;

/**
 * Represents a Country record from the countries table
 */
public class Country {
    /**
     * The Primary Key
     */
    private final int countryID;
    /**
     * This Country's name
     */
    private final String name;

    /**
     * Constructs a Country object
     * @param countryID Primary Key
     * @param name Country name
     */
    public Country(int countryID, String name) {
        this.countryID = countryID;
        this.name = name;
    }
    /**
     * Overrides the toString() method in order to properly display in ComboBoxes
     * @return A String representation of the Country instance
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Returns this Country's name
     * @return Country's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns this Country's countryID
     * @return Primary Key
     */
    public int getCountryID() {
        return countryID;
    }
}
