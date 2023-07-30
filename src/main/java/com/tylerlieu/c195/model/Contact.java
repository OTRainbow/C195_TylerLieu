package com.tylerlieu.c195.model;

/**
 * Represents a Contact record from the contacts table
 */
public class Contact {
    /**
     * The Primary Key
     */
    private final int contactID;
    /**
     * This Contact's name
     */
    private final String name;
    /**
     * This Contact's email address
     */
    private final String email;

    /**
     * Constructs a Contact object
     * @param contactID Primary Key
     * @param name Contact name
     * @param email Contact email
     */
    public Contact(int contactID, String name, String email) {
        this.contactID = contactID;
        this.name = name;
        this.email = email;
    }
    /**
     * Overrides the toString() method in order to properly display in ComboBoxes
     * @return A String representation of the Contact instance
     */
    @Override
    public String toString() {
        return (contactID + " " + name + " " + email);
    }

    /**
     * Returns this Contact's contactID
     * @return Primary Key
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Returns this Contact's name
     * @return Contact's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns this Contact's email address
     * @return Contact's email address
     */
    public String getEmail() {
        return email;
    }
}
