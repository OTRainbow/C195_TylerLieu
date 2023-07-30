package com.tylerlieu.c195.model;

/**
 * Represents a Division record from the first_level_divisions table
 */
public class Division {
    /**
     * The Primary Key
     */
    private final int divisionID;
    /**
     * This Division's name
     */
    private final String name;
    /**
     * A Foreign Key for Country records
     */
    private final int countryID;

    /**
     * Constructs a Division object
     * @param divisionID Primary Key
     * @param name Division name
     * @param countryID Foreign Key for Country
     */
    public Division(int divisionID, String name, int countryID) {
        this.divisionID = divisionID;
        this.name = name;
        this.countryID = countryID;
    }

    /**
     * Overrides the toString() method in order to properly display in ComboBoxes
     * @return A String representation of this Division instance
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Returns this Division's name
     * @return Division's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns this Division's divisionID
     * @return Primary Key
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * Returns this Division's Country's countryID
     * @return Foreign Key for Country
     */
    public int getCountryID() {
        return countryID;
    }
}
