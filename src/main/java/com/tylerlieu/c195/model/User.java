package com.tylerlieu.c195.model;

public class User {
    private int userID;
    private String userName;
    public User(int userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }
    @Override
    public String toString() {
        return (Integer.toString(userID) + " " + userName);
    }
    public int getUserID() {
        return this.userID;
    }
    public String getUserName() {
        return this.userName;
    }
}
