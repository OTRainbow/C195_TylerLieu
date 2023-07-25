package com.tylerlieu.c195.DAO;

import com.tylerlieu.c195.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AppointmentsQuery {
    public static ObservableList<Appointment> getAllAppointmentRecordsByUserID() throws SQLException {
        String query = "" +
            "SELECT " +
                "app.Appointment_ID as appointmentID, " +
                "app.Title as title, " +
                "app.Description as description, " +
                "app.Location as location, " +
                "app.Type as type, " +
                "app.Start as startDate, " +
                "app.End as endDate, " +
                "app.Customer_ID as customerID, " +
                "cus.Customer_Name as customerName, " +
                "app.User_ID as userID, " +
                "usr.User_Name as userName, " +
                "app.Contact_ID as contactID, " +
                "con.Contact_Name as contactName, " +
                "con.Email as contactEmail " +
            "FROM appointments as app " +
                "INNER JOIN customers as cus " +
                "ON app.Customer_ID = cus.Customer_ID " +
                "INNER JOIN users as usr " +
                "ON app.User_ID = usr.User_ID " +
                "INNER JOIN contacts as con " +
                "ON app.Contact_ID = con.Contact_ID;";
        Statement statement = DB.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            appointmentsList.add(new Appointment(
                    resultSet.getInt("appointmentID"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("location"),
                    resultSet.getString("type"),
                    resultSet.getDate("startDate"),
                    resultSet.getDate("endDate"),
                    resultSet.getInt("customerID"),
                    resultSet.getString("customerName"),
                    resultSet.getInt("userID"),
                    resultSet.getString("userName"),
                    resultSet.getInt("contactID"),
                    resultSet.getString("contactName"),
                    resultSet.getString("contactEmail")
            ));
        }
        resultSet.close();
        statement.close();
        return appointmentsList;
    }
}
