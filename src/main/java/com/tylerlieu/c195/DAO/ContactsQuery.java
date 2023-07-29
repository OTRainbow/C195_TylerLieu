package com.tylerlieu.c195.DAO;

import com.tylerlieu.c195.model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContactsQuery {
    public static ObservableList<Contact> getAllContacts() throws SQLException {
        String query = "SELECT Contact_ID as contactID, Contact_Name as name, Email as email FROM contacts;";
        Statement statement = DB.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        ObservableList<Contact> contactsList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            contactsList.add(new Contact(
                resultSet.getInt("contactID"),
                resultSet.getString("name"),
                resultSet.getString("email")
            ));
        }
        resultSet.close();
        statement.close();
        return contactsList;
    }
}
