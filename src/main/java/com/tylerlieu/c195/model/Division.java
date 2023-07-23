package com.tylerlieu.c195.model;

public class Division {
    // From first_level_divisions table
    private int divisionID;
    private String name;
    private int countryID;

    // Derived from countryID
    private Country country;
}
