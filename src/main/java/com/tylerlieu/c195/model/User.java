package com.tylerlieu.c195.model;
/**
 * Represents a User record from the users table.
 */
public class User {
    /**
     * The Primary Key
     */
    private final int userID;
    /**
     * This User's Name
     */
    private final String userName;

    /**
     * Constructs a User object
     * @param userID Primary Key
     * @param userName User name
     */
    public User(int userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }

    /**
     * Overrides the toString() method in order to properly display in ComboBoxes
     * @return A String representation of this User instance
     */
    @Override
    public String toString() {
        return (userID + " " + userName);
    }

    /**
     * Returns this User's userID
     * @return Primary Key
     */
    public int getUserID() {
        return this.userID;
    }

    /**
     * Returns this User's name
     * @return User's name
     */
    public String getUserName() {
        return this.userName;
    }
}
