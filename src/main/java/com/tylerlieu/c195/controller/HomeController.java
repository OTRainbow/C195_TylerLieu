package com.tylerlieu.c195.controller;

import com.tylerlieu.c195.DAO.AppointmentsQuery;
import com.tylerlieu.c195.DAO.ContactsQuery;
import com.tylerlieu.c195.DAO.CustomersQuery;
import com.tylerlieu.c195.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    public Appointment upcomingAppointment;
    public Label lblUpcomingAppointment;
    public Label lblSignedInAs;
    public ComboBox<Contact> cboxContact;
    public ObservableList<Contact> contactsList;
    public TableView<AppointmentTypeCount> appointmentTypesTable;
    public TableColumn<AppointmentTypeCount, Integer> yearColumn;
    public TableColumn<AppointmentTypeCount, Month> monthColumn;
    public TableColumn<AppointmentTypeCount, String> typeColumn;
    public TableColumn<AppointmentTypeCount, Integer> totalAppointmentsColumn;
    public TableView<Appointment> contactScheduleTable;
    public TableColumn<Appointment, Integer> appointmentIDColumn;
    public TableColumn<Appointment, String> titleColumn;
    public TableColumn<Appointment, String> contactAppointmentTypeColumn;
    public TableColumn<Appointment, String> descriptionColumn;
    public TableColumn<Appointment, String> startColumn;
    public TableColumn<Appointment, String> endColumn;
    public TableColumn<Appointment, Integer> customerIDColumn;
    public TableView<CustomerDivisionCount> customerDivisionsTable;
    public TableColumn<CustomerDivisionCount, String> countryColumn;
    public TableColumn<CustomerDivisionCount, String> divisionColumn;
    public TableColumn<CustomerDivisionCount, Integer> totalCustomersColumn;
    public static ObservableList<AppointmentTypeCount> appointmentTypesList = FXCollections.observableArrayList();
    public static ObservableList<Appointment> contactAppointmentsList = FXCollections.observableArrayList();
    public static ObservableList<CustomerDivisionCount> customerDivisionsList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alertUpcomingAppointment();
        lblSignedInAs.setText("Signed In As: " + Session.user.getUserName());

        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        totalAppointmentsColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        contactAppointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startDateLocalDisplay"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endDateLocalDisplay"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        countryColumn.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        totalCustomersColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        try {
            contactsList = ContactsQuery.getAllContacts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        cboxContact.setItems(contactsList);
        cboxContact.setValue(contactsList.get(0));

        appointmentTypesTable.setItems(appointmentTypesList);
        contactScheduleTable.setItems(contactAppointmentsList);
        customerDivisionsTable.setItems(customerDivisionsList);

        loadAppointmentTypesTable();
        loadContactScheduleTable(cboxContact.getValue());
        loadCustomerDivisionsTable();
    }
    public static void loadAppointmentTypesTable() {
        try {
            appointmentTypesList.clear();
            appointmentTypesList.addAll(AppointmentsQuery.getAppointmentTypeCountRecords());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void loadContactScheduleTable(Contact contact) {
        try {
            contactAppointmentsList.clear();
            contactAppointmentsList.addAll(AppointmentsQuery.getAppointmentsByContact(contact));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void loadCustomerDivisionsTable() {
        try {
            customerDivisionsList.clear();
            customerDivisionsList.addAll(CustomersQuery.getCustomerDivisionCountRecords());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void alertUpcomingAppointment() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("You have no upcoming appointments within the next 15 minutes.");
        try {
            upcomingAppointment = AppointmentsQuery.findUpcomingAppointment();
            if (upcomingAppointment != null) {
                alert.setContentText("You have appointment id#" + upcomingAppointment.getAppointmentID() + " at " + upcomingAppointment.getStartDateLocal().format(DateTimeFormatter.ofPattern("h:mma MM/dd/yyyy")) + ".");
                lblUpcomingAppointment.setText("Upcoming Appointment: #" + upcomingAppointment.getAppointmentID() + " at " + upcomingAppointment.getStartDateLocal().format(DateTimeFormatter.ofPattern("h:mma MM/dd/yyyy")));
            }
            alert.showAndWait();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onContactSelect(ActionEvent actionEvent) {
        loadContactScheduleTable(cboxContact.getValue());
    }
}
