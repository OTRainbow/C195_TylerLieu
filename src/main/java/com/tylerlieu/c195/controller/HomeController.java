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

/** This is the controller for managing the homeTab.fxml view. */
public class HomeController implements Initializable {
    /**
     * If there is an Appointment for the user coming up in the next 15 minutes, this will be that Appointment.
     */
    public Appointment upcomingAppointment;
    /**
     * A label that displays whether the user has an upcoming appointment
     */
    public Label lblUpcomingAppointment;
    /**
     * A label that displays the name of the user that is logged in
     */
    public Label lblSignedInAs;
    /**
     * A ComboBox for selecting a Contact
     */
    public ComboBox<Contact> cboxContact;
    /**
     * A list of Contacts to populate the combobox
     */
    public ObservableList<Contact> contactsList;
    /**
     * A table representing how many of each appointment type exists for each month of each year
     */
    public TableView<AppointmentTypeCount> appointmentTypesTable;
    /**
     * A column for appointmentTypesTable showing the year
     */
    public TableColumn<AppointmentTypeCount, Integer> yearColumn;
    /**
     * A column for appointmentTypesTable showing the month
     */
    public TableColumn<AppointmentTypeCount, Month> monthColumn;
    /**
     * A column for appointmentTypesTable showing the appointment type
     */
    public TableColumn<AppointmentTypeCount, String> typeColumn;
    /**
     * A column for appointmentTypesTable showing the total number of appointment types
     */
    public TableColumn<AppointmentTypeCount, Integer> totalAppointmentsColumn;
    /**
     * A table for showing an appointment schedule for each Contact
     */
    public TableView<Appointment> contactScheduleTable;
    /**
     * A column for contactScheduleTable showing the appointmentID
     */
    public TableColumn<Appointment, Integer> appointmentIDColumn;
    /**
     * A column for contactScheduleTable showing the appointment title
     */
    public TableColumn<Appointment, String> titleColumn;
    /**
     * A column for contactScheduleTable showing the appointment type
     */
    public TableColumn<Appointment, String> contactAppointmentTypeColumn;
    /**
     * A column for contactScheduleTable showing the appointment description
     */
    public TableColumn<Appointment, String> descriptionColumn;
    /**
     * A column for contactScheduleTable showing the appointment start date/time
     */
    public TableColumn<Appointment, String> startColumn;
    /**
     * A column for contactScheduleTable showing the appointment end date/time
     */
    public TableColumn<Appointment, String> endColumn;
    /**
     * A column for contactScheduleTable showing the customerID
     */
    public TableColumn<Appointment, Integer> customerIDColumn;
    /**
     * A table for showing how many customers live in each division
     */
    public TableView<CustomerDivisionCount> customerDivisionsTable;
    /**
     * A column for customerDivisionsTable showing the country name
     */
    public TableColumn<CustomerDivisionCount, String> countryColumn;
    /**
     * A column for customerDivisionsTable showing the division name
     */
    public TableColumn<CustomerDivisionCount, String> divisionColumn;
    /**
     * A column for customerDivisionsTable showing the number of customers that live in a division
     */
    public TableColumn<CustomerDivisionCount, Integer> totalCustomersColumn;
    /**
     * A list of AppointmentTypeCount objects to populate appointmentTypesTable
     */
    public static ObservableList<AppointmentTypeCount> appointmentTypesList = FXCollections.observableArrayList();
    /**
     * A list of Appointment objects to populate contactScheduleTable
     */
    public static ObservableList<Appointment> contactAppointmentsList = FXCollections.observableArrayList();
    /**
     * A list of CustomerDivisionCOunt objects to populate customerDivisionsTable
     */
    public static ObservableList<CustomerDivisionCount> customerDivisionsList = FXCollections.observableArrayList();
    /**
     * The Contact that is currently selected from cboxContact
     */
    public static Contact selectedContact;

    /**
     * This method is called as soon as the view begins to load.
     *
     * First, it calls the alert method upon logging in and changes label texts.
     *
     * Then, it sets up CellValueFactory for each column in the three tables.
     *
     * Then, it sets up cboxContact and sets the ObservableLists to the tables.
     *
     * Finally, it calls the three table loading methods.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Call alert upon logging in
        alertUpcomingAppointment();
        lblSignedInAs.setText("Signed In As: " + Session.user.getUserName());

        // Set up appointmentTypesTable
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        totalAppointmentsColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Set up contactScheduleTable
        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        contactAppointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startDateLocalDisplay"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endDateLocalDisplay"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        // Set up customerDivisionsTable
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        totalCustomersColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Populate contactsList
        try {
            contactsList = ContactsQuery.getAllContacts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Set up cboxContact, select first Contact by default
        cboxContact.setItems(contactsList);
        selectedContact = contactsList.get(0);
        cboxContact.setValue(selectedContact);

        // Set tables to ObservableLists
        appointmentTypesTable.setItems(appointmentTypesList);
        contactScheduleTable.setItems(contactAppointmentsList);
        customerDivisionsTable.setItems(customerDivisionsList);

        // Call table loading methods
        loadAppointmentTypesTable();
        loadContactScheduleTable(cboxContact.getValue());
        loadCustomerDivisionsTable();
    }

    /**
     * This method queries the appointments table and populates the TableView appointmentTypesTable.
     */
    public static void loadAppointmentTypesTable() {
        try {
            appointmentTypesList.clear();
            appointmentTypesList.addAll(AppointmentsQuery.getAppointmentTypeCountRecords());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method queries the appointments table and populates the TableView contactScheduleTable.
     * @param contact This Contact is user selected
     */
    public static void loadContactScheduleTable(Contact contact) {
        try {
            contactAppointmentsList.clear();
            contactAppointmentsList.addAll(AppointmentsQuery.getAppointmentsByContact(contact));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method queries the customers table and populates the TableView customerDivisionsTable.
     */
    public static void loadCustomerDivisionsTable() {
        try {
            customerDivisionsList.clear();
            customerDivisionsList.addAll(CustomersQuery.getCustomerDivisionCountRecords());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is called as soon as the user logs in.
     * It calls <code>AppointmentsQuery.findUpcomingAppointment()</code> to find an upcoming appointment.
     * It displays an appropriate alert depending on the outcome.
     */
    public void alertUpcomingAppointment() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("You have no upcoming appointments within the next 15 minutes.");
        try {
            upcomingAppointment = AppointmentsQuery.findUpcomingAppointment();
            if (upcomingAppointment != null) {
                alert.setContentText("You have appointment #" + upcomingAppointment.getAppointmentID() + " at " + upcomingAppointment.getStartDateLocal().format(DateTimeFormatter.ofPattern("h:mma MM/dd/yyyy")) + ".");
                lblUpcomingAppointment.setText("Upcoming Appointment: #" + upcomingAppointment.getAppointmentID() + " at " + upcomingAppointment.getStartDateLocal().format(DateTimeFormatter.ofPattern("h:mma MM/dd/yyyy")));
            }
            alert.showAndWait();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is called anytime the user selects a different Contact from the combobox.
     * It reassigns selectedContact with the newly selected Contact.
     * @param actionEvent
     */
    public void onContactSelect(ActionEvent actionEvent) {
        selectedContact = cboxContact.getValue();
        loadContactScheduleTable(selectedContact);
    }
}
