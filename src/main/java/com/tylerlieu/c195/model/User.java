package com.tylerlieu.c195.model;

public class User {
    // From users table
    private int userID;
    private String username;
    public User(int userID, String username) {
        this.userID = userID;
        this.username = username;
    }
    public int getUserID() {
        return this.userID;
    }
    public String getUsername() {
        return this.username;
    }
}
