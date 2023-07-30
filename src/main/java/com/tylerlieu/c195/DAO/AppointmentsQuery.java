package com.tylerlieu.c195.DAO;

import com.tylerlieu.c195.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
/**
 * Static class for methods used to perform queries on the appointments table
 */
public class AppointmentsQuery {
    /**
     * Retrieve all Appointments from the database.
     * @return An ObservableList of Appointment objects
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllAppointmentRecords() throws SQLException {
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
                    resultSet.getTimestamp("startDate").toLocalDateTime().atZone(ZoneOffset.UTC),
                    resultSet.getTimestamp("endDate").toLocalDateTime().atZone(ZoneOffset.UTC),
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
    /**
     * Adds an Appointment to the database
     * @param appointment Primary Key not needed
     * @throws SQLException
     */
    public static void addAppointment(Appointment appointment) throws SQLException {
        String query = "" +
            "INSERT INTO appointments( " +
                "Title, " +
                "Description, " +
                "Location, " +
                "Type, " +
                "Start, " +
                "End, " +
                "Create_Date, " +
                "Created_By, " +
                "Last_Update, " +
                "Last_Updated_By, " +
                "Customer_ID, " +
                "User_ID, " +
                "Contact_ID " +
                ") " +
            "VALUES ( ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?, ?, ? );";
        PreparedStatement statement = DB.connection.prepareStatement(query);
        statement.setString(1, appointment.getTitle());
        statement.setString(2, appointment.getDescription());
        statement.setString(3, appointment.getLocation());
        statement.setString(4, appointment.getType());
        statement.setString(5, appointment.getStartDateUTC().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        statement.setString(6, appointment.getEndDateUTC().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        statement.setString(7, Session.user.getUserName());
        statement.setString(8, Session.user.getUserName());
        statement.setInt(9, appointment.getCustomerID());
        statement.setInt(10, appointment.getUserID());
        statement.setInt(11, appointment.getContactID());
        statement.executeUpdate();
        statement.close();
    }
    /**
     * Updates an existing Appointment record in the database
     * @param updatedAppointment Requires Primary Key
     * @throws SQLException
     */
    public static void updateAppointment(Appointment updatedAppointment) throws SQLException {
        String query = "" +
            "UPDATE appointments " +
            "SET " +
                "Title = ?, " +
                "Description = ?, " +
                "Location = ?, " +
                "Type = ?, " +
                "Start = ?, " +
                "End = ?, " +
                "Last_Update = NOW(), " +
                "Last_Updated_By = ?, " +
                "Customer_ID = ?, " +
                "User_ID = ?, " +
                "Contact_ID = ? " +
            "WHERE Appointment_ID = ?;";
        PreparedStatement statement = DB.connection.prepareStatement(query);
        statement.setString(1, updatedAppointment.getTitle());
        statement.setString(2, updatedAppointment.getDescription());
        statement.setString(3, updatedAppointment.getLocation());
        statement.setString(4, updatedAppointment.getType());
        statement.setString(5, updatedAppointment.getStartDateUTC().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        statement.setString(6, updatedAppointment.getEndDateUTC().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        statement.setString(7, Session.user.getUserName());
        statement.setInt(8, updatedAppointment.getCustomerID());
        statement.setInt(9, updatedAppointment.getUserID());
        statement.setInt(10, updatedAppointment.getContactID());
        statement.setInt(11, updatedAppointment.getAppointmentID());
        statement.executeUpdate();
        statement.close();
    }
    /**
     * Deletes an Appointment from the database
     * @param appointment Requires Primary Key
     * @throws SQLException
     */
    public static void deleteAppointment(Appointment appointment) throws SQLException {
        String query = "DELETE FROM appointments WHERE Appointment_ID = ?;";
        PreparedStatement statement = DB.connection.prepareStatement(query);
        statement.setInt(1, appointment.getAppointmentID());
        statement.executeUpdate();
        statement.close();
    }

    /**
     * Deletes all Appointments assigned to a single Customer
     * @param customer Requires Primary Key
     * @throws SQLException
     */
    public static void deleteAppointmentsByCustomerID(Customer customer) throws SQLException {
        String query = "DELETE FROM appointments WHERE Customer_ID = ?;";
        PreparedStatement statement = DB.connection.prepareStatement(query);
        statement.setInt(1, customer.getCustomerID());
        statement.executeUpdate();
        statement.close();
    }

    /**
     * Looks for an Appointment that starts sometime between now and 15 minutes
     * @return Appointment object
     * @throws SQLException
     */
    public static Appointment findUpcomingAppointment() throws SQLException {
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime endTime = currentTime.plusMinutes(15);

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
                "ON app.Contact_ID = con.Contact_ID " +
            "WHERE " +
                "app.User_ID = ? AND " +
                "app.Start >= ? AND " +
                "app.Start <= ? " +
            "ORDER BY app.Start DESC;";
        PreparedStatement statement = DB.connection.prepareStatement(query);
        statement.setInt(1, Session.user.getUserID());
        statement.setString(2, currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        statement.setString(3, endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Appointment appointment = new Appointment(
                    resultSet.getInt("appointmentID"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("location"),
                    resultSet.getString("type"),
                    resultSet.getTimestamp("startDate").toLocalDateTime().atZone(ZoneOffset.UTC),
                    resultSet.getTimestamp("endDate").toLocalDateTime().atZone(ZoneOffset.UTC),
                    resultSet.getInt("customerID"),
                    resultSet.getString("customerName"),
                    resultSet.getInt("userID"),
                    resultSet.getString("userName"),
                    resultSet.getInt("contactID"),
                    resultSet.getString("contactName"),
                    resultSet.getString("contactEmail")
            );
            resultSet.close();
            statement.close();
            return appointment;
        } else {
            return null;
        }
    }

    /**
     * Retrieves all Appointments that are assigned to as specific Contact
     * REQUIREMENT 3f: Used to create one of the three required reports
     * @param contact Requires Primary Key
     * @return An ObservableList of Appointments with matching Contact
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAppointmentsByContact(Contact contact) throws SQLException {
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
                "ON app.Contact_ID = con.Contact_ID " +
            "WHERE app.Contact_ID = ? " +
            "ORDER BY app.Start DESC;";
        PreparedStatement statement = DB.connection.prepareStatement(query);
        statement.setInt(1, contact.getContactID());
        ResultSet resultSet = statement.executeQuery();

        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            appointmentsList.add(new Appointment(
                    resultSet.getInt("appointmentID"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("location"),
                    resultSet.getString("type"),
                    resultSet.getTimestamp("startDate").toLocalDateTime().atZone(ZoneOffset.UTC),
                    resultSet.getTimestamp("endDate").toLocalDateTime().atZone(ZoneOffset.UTC),
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

    /**
     * Retrieves a count of the total number of appointments of each type by each month of each year
     * REQUIREMENT 3f: Used to create one of the three required reports
     * @return An ObservableList of AppointmentTypeCount objects
     * @throws SQLException
     */
    public static ObservableList<AppointmentTypeCount> getAppointmentTypeCountRecords() throws SQLException {
        String query = "SELECT YEAR(Start) AS Year, MONTH(Start) AS Month, Type, COUNT(*) AS Total FROM appointments GROUP BY YEAR(Start), MONTH(Start), Type ORDER BY Year, Month;";
        Statement statement = DB.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        ObservableList<AppointmentTypeCount> appointmentTypesList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            appointmentTypesList.add(new AppointmentTypeCount(
                    resultSet.getInt("Year"),
                    resultSet.getInt("Month"),
                    resultSet.getString("Type"),
                    resultSet.getInt("Total")
            ));
        }
        resultSet.close();
        statement.close();
        return appointmentTypesList;
    }
}
