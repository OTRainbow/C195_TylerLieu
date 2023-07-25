package com.tylerlieu.c195.model;

public class Contact {
    private int contactID;
    private String name;
    private String email;
    public Contact(int contactID, String name, String email) {
        this.contactID = contactID;
        this.name = name;
        this.email = email;
    }

    public int getContactID() {
        return contactID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
