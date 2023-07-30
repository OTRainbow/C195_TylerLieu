package com.tylerlieu.c195.controller;

import com.tylerlieu.c195.DAO.AppointmentsQuery;
import com.tylerlieu.c195.DAO.ContactsQuery;
import com.tylerlieu.c195.DAO.CustomersQuery;
import com.tylerlieu.c195.DAO.UsersQuery;
import com.tylerlieu.c195.model.Appointment;
import com.tylerlieu.c195.model.Contact;
import com.tylerlieu.c195.model.Customer;
import com.tylerlieu.c195.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/** This is the controller for managing the appointmentsUpdateForm.fxml view. */
public class AppointmentsUpdateController implements Initializable {
    /**
     * A field for appointmentID
     */
    public TextField fldID;
    /**
     * A field for appointment title
     */
    public TextField fldTitle;
    /**
     * A field for appointment description
     */
    public TextArea fldDescription;
    /**
     * A field for appointment location
     */
    public TextField fldLocation;
    /**
     * A field for appointment type
     */
    public TextField fldType;
    /**
     * A combobox to select a Contact
     */
    public ComboBox<Contact> cboxContact;
    /**
     * A list of Contacts to populate cboxContact
     */
    public ObservableList<Contact> contactsList;
    /**
     * A combobox to select a Customer
     */
    public ComboBox<Customer> cboxCustomer;
    /**
     * A list of Customers to populate cboxCustomer
     */
    public ObservableList<Customer> customersList;
    /**
     * A combobox to select a User
     */
    public ComboBox<User> cboxUser;
    /**
     * A list of Users to populate cboxUser
     */
    public ObservableList<User> usersList;
    /**
     * A date picker to pick the date
     */
    public DatePicker datePicker;
    /**
     * A combobox to select the start time
     */
    public ComboBox<ZonedDateTime> cboxStartTime;
    /**
     * A combobox to select the end time
     */
    public ComboBox<ZonedDateTime> cboxEndTime;
    /**
     * A label for start time
     */
    public Label lblStartTime;
    /**
     * A label for end time
     */
    public Label lblEndTime;
    /**
     * An Alert to show if the user leaves a field blank when they click update
     */
    public Alert blankFields;
    /**
     * An Alert to show if updating the customer in the database fails
     */
    public Alert attemptFailed;
    /**
     * An Alert to show if the appointment overlaps with another appointment
     */
    public Alert overlapIssue;

    /**
     * An Alert to show if the appointment is outside of business hours
     */
    public Alert businessHours;
    /**
     * An object holding the Appointment that was selected in the previous form
     */
    public Appointment oldAppointment = AppointmentsController.selectedAppointment;

    /**
     * This method is called as soon as the view begins to load.
     * First, it loads all the alerts.
     * Then, it populates contactsList, customersList, usersList.
     * Then, it populates cboxContact, cboxCustomer, cboxUser.
     * Then, it fills in all values except cboxStartTime and cboxEndTime.
     * Then, it calls setUpTimeBoxes().
     * Finally, it populates and sets both cboxStartTime and cboxEndTime.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load Alerts
        blankFields = new Alert(Alert.AlertType.ERROR);
        blankFields.setContentText("One or more items have been left blank. Please fill in all fields.");
        attemptFailed = new Alert(Alert.AlertType.ERROR);
        attemptFailed.setContentText("Failed to update appointment in database");
        overlapIssue = new Alert(Alert.AlertType.ERROR);
        overlapIssue.setContentText("Unable to update appointment because it overlaps with an existing appointment");
        businessHours = new Alert(Alert.AlertType.ERROR);
        businessHours.setContentText("Unable to update appointment because it exceeds businesss hours (8AM to 10PM Eastern Time)");

        // Populate Lists
        try {
            contactsList = ContactsQuery.getAllContacts();
            customersList = CustomersQuery.getAllCustomerRecords();
            usersList = UsersQuery.getAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Populate comboboxes
        cboxContact.setItems(contactsList);
        cboxCustomer.setItems(customersList);
        cboxUser.setItems(usersList);

        // Fill in values
        fldID.setText(Integer.toString(oldAppointment.getAppointmentID()));
        fldTitle.setText(oldAppointment.getTitle());
        fldDescription.setText(oldAppointment.getDescription());
        fldLocation.setText(oldAppointment.getLocation());
        fldType.setText(oldAppointment.getType());
        cboxContact.setValue(oldAppointment.getContact());
        cboxCustomer.setValue(oldAppointment.getCustomer());
        cboxUser.setValue(oldAppointment.getUser());
        datePicker.setValue(oldAppointment.getStartDateLocal().toLocalDate());

        // Set custom cell factories for time boxes
        setUpTimeBoxes();

        // Populate startTime combobox
        ObservableList<ZonedDateTime> startTimeSelectionList = FXCollections.observableArrayList();
        ZonedDateTime iStartTime = ZonedDateTime.of(datePicker.getValue(), LocalTime.of(8,0), ZoneId.of("America/New_York")).withZoneSameInstant(ZoneId.systemDefault());
        ZonedDateTime lastStartTime = ZonedDateTime.of(datePicker.getValue(), LocalTime.of(22,0), ZoneId.of("America/New_York")).withZoneSameInstant(ZoneId.systemDefault());
        while (iStartTime.isBefore(lastStartTime)) {
            startTimeSelectionList.add(iStartTime);
            iStartTime = iStartTime.plusMinutes(5);
        }
        cboxStartTime.setItems(startTimeSelectionList);
        cboxStartTime.setValue(oldAppointment.getStartDateLocal());

        // Populate endTime combobox
        ObservableList<ZonedDateTime> endTimeSelectionList = FXCollections.observableArrayList();
        ZonedDateTime iEndTime = cboxStartTime.getValue().plusMinutes(5);
        ZonedDateTime lastEndTime = ZonedDateTime.of(datePicker.getValue(), LocalTime.of(22,0), ZoneId.of("America/New_York")).withZoneSameInstant(ZoneId.systemDefault());
        while (!iEndTime.isAfter(lastEndTime)) {
            endTimeSelectionList.add(iEndTime);
            iEndTime = iEndTime.plusMinutes(5);
        }
        cboxEndTime.setItems(endTimeSelectionList);
        cboxEndTime.setValue(oldAppointment.getEndDateLocal());
    }

    /**
     * This method is called when the user clicks the update button.
     * First, it checks for input errors.
     * Then, it creates a new Appointment object.
     * Then, it attempts to run AppointmentsQuery.updateAppointment(Appointment).
     * If successful, the method reloads all the tables and closes the form stage.
     * @param actionEvent Used to get current stage
     */
    public void onButtonUpdateClick(ActionEvent actionEvent) {
        // Check for errors
        if (areThereAnyBlankFields()) {
            blankFields.showAndWait();
            return;
        }
        if (doesThisOverlap()) {
            overlapIssue.showAndWait();
            return;
        }
        if (isThisOutOfBusinessHours()) {
            businessHours.showAndWait();
            return;
        }
        // Construct new appointment
        Appointment appointment = new Appointment(
                oldAppointment.getAppointmentID(),
                fldTitle.getText(),
                fldDescription.getText(),
                fldLocation.getText(),
                fldType.getText(),
                cboxStartTime.getValue(),
                cboxEndTime.getValue(),
                cboxCustomer.getValue().getCustomerID(),
                cboxCustomer.getValue().getName(),
                cboxUser.getValue().getUserID(),
                cboxUser.getValue().getUserName(),
                cboxContact.getValue().getContactID(),
                cboxContact.getValue().getName(),
                cboxContact.getValue().getEmail()
        );
        // Update Appointment in database
        try {
            AppointmentsQuery.updateAppointment(appointment);
        } catch (SQLException e) {
            attemptFailed.showAndWait();
            throw new RuntimeException(e);
        }
        // Reload tables
        AppointmentsController.loadTable();
        HomeController.loadAppointmentTypesTable();
        HomeController.loadContactScheduleTable(HomeController.selectedContact);
        // Close the form
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
    /**
     * This method is called when the user clicks on the cancel button.
     * It simply closes the form stage so that the user can return to the main view.
     * @param actionEvent Used to get current stage
     */
    public void onButtonCancelClick(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
    /**
     * This method is called when the user changes the selected date in datePicker.
     * It runs a loop that generates a startTimeSelectionList that can be used to populate cboxStartTime.
     */
    public void onDateSelected() {
        // Enable and disable
        if (datePicker.getValue() == null) {
            lblStartTime.setDisable(true);
            cboxStartTime.setDisable(true);
            cboxStartTime.valueProperty().set(null);
            lblEndTime.setDisable(true);
            cboxEndTime.setDisable(true);
            cboxEndTime.valueProperty().set(null);
        } else {
            lblStartTime.setDisable(false);
            cboxStartTime.setDisable(false);
            cboxStartTime.valueProperty().set(null);
            lblEndTime.setDisable(true);
            cboxEndTime.setDisable(true);
            cboxEndTime.valueProperty().set(null);
            // Loop to populate cboxStartTime
            ObservableList<ZonedDateTime> startTimeSelectionList = FXCollections.observableArrayList();
            ZonedDateTime iTime = ZonedDateTime.of(datePicker.getValue(), LocalTime.of(8,0), ZoneId.of("America/New_York")).withZoneSameInstant(ZoneId.systemDefault());
            ZonedDateTime lastTime = ZonedDateTime.of(datePicker.getValue(), LocalTime.of(22,0), ZoneId.of("America/New_York")).withZoneSameInstant(ZoneId.systemDefault());
            while (iTime.isBefore(lastTime)) {
                startTimeSelectionList.add(iTime);
                iTime = iTime.plusMinutes(5);
            }
            cboxStartTime.setItems(startTimeSelectionList);
        }
    }
    /**
     * This method is called when the user selects a start time.
     * It runs a loop that generates a endTimeSelectionList that can be used to populate cboxEndTime.
     */
    public void onStartTimeSelect() {
        // Reset and Enable end time
        lblEndTime.setDisable(false);
        cboxEndTime.setDisable(false);
        cboxEndTime.valueProperty().set(null);
        if (cboxStartTime.getValue() == null) {
            return;
        }
        // Loop to populate cboxEndTime
        ObservableList<ZonedDateTime> endTimeSelectionList = FXCollections.observableArrayList();
        ZonedDateTime iTime = cboxStartTime.getValue().plusMinutes(5);
        ZonedDateTime lastTime = ZonedDateTime.of(datePicker.getValue(), LocalTime.of(22,0), ZoneId.of("America/New_York")).withZoneSameInstant(ZoneId.systemDefault());
        while (!iTime.isAfter(lastTime)) {
            endTimeSelectionList.add(iTime);
            iTime = iTime.plusMinutes(5);
        }
        cboxEndTime.setItems(endTimeSelectionList);
    }

    /**
     * Validates input by checking for blank fields
     * @return True if there are any blank fields
     */
    public Boolean areThereAnyBlankFields() {
        return fldTitle.getText().isEmpty() ||
                fldDescription.getText().isEmpty() ||
                fldLocation.getText().isEmpty() ||
                fldType.getText().isEmpty() ||
                cboxContact.getValue() == null ||
                cboxCustomer.getValue() == null ||
                cboxUser.getValue() == null ||
                datePicker.getValue() == null ||
                cboxStartTime.getValue() == null ||
                cboxEndTime.getValue() == null;
    }
    /**
     * Validates input by making sure appointments never overlap for customers
     * @return True if there is an overlapping appointment
     */
    public Boolean doesThisOverlap() {
        for (Appointment existingAppointment : AppointmentsController.appointmentsList) {
            if (existingAppointment.getAppointmentID() == oldAppointment.getAppointmentID()) {
                continue;
            }
            if (!(existingAppointment.getCustomerID() == cboxCustomer.getValue().getCustomerID())) {
                continue;
            }
            if (cboxStartTime.getValue().isAfter(existingAppointment.getStartDateLocal()) && cboxStartTime.getValue().isBefore(existingAppointment.getEndDateLocal())) {
                return true;
            }
            if (cboxEndTime.getValue().isAfter(existingAppointment.getStartDateLocal()) && cboxEndTime.getValue().isBefore(existingAppointment.getEndDateLocal())) {
                return true;
            }
            if (existingAppointment.getStartDateLocal().isAfter(cboxStartTime.getValue()) && existingAppointment.getStartDateLocal().isBefore(cboxEndTime.getValue())) {
                return true;
            }
            if (existingAppointment.getEndDateLocal().isAfter(cboxStartTime.getValue()) && existingAppointment.getEndDateLocal().isBefore(cboxEndTime.getValue())) {
                return true;
            }
            if (cboxStartTime.getValue().equals(existingAppointment.getStartDateLocal())) {
                return true;
            }
        }
        return false;
    }
    /**
     * Validates input by checking if start or end times fall outside business hours.
     * In theory, this should always be false since there are measures to prevent such selection.
     * @return True if any time is out of business hours
     */
    public Boolean isThisOutOfBusinessHours() {
        // This exception should never happen, but exists to satisfy requirement 3d
        if (cboxStartTime.getValue().isBefore(ZonedDateTime.of(datePicker.getValue(), LocalTime.of(8,0), ZoneId.of("America/New_York")).withZoneSameInstant(ZoneId.systemDefault()))) {
            return true;
        }
        if (cboxEndTime.getValue().isBefore(ZonedDateTime.of(datePicker.getValue(), LocalTime.of(8,0), ZoneId.of("America/New_York")).withZoneSameInstant(ZoneId.systemDefault()))) {
            return true;
        }
        if (cboxStartTime.getValue().isAfter(ZonedDateTime.of(datePicker.getValue(), LocalTime.of(22,0), ZoneId.of("America/New_York")).withZoneSameInstant(ZoneId.systemDefault()))) {
            return true;
        }
        if (cboxEndTime.getValue().isAfter(ZonedDateTime.of(datePicker.getValue(), LocalTime.of(22,0), ZoneId.of("America/New_York")).withZoneSameInstant(ZoneId.systemDefault()))) {
            return true;
        }
        return false;
    }
    /**
     * This method takes a ZonedDateTime object and formats it into a String that shows the time.
     * Only used for setUpTimeBoxes().
     * @param zdt A ZonedDateTime to be formatted
     * @return formatted time as String
     */
    public String formatZonedDateTime(ZonedDateTime zdt) {
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h:mm a");
        return zdt.format(timeFormat);
    }
    /**
     * The purpose of this method is to allow the two comboboxes to contain ZonedDateTime objects but display them as Strings of time.
     * Both comboboxes use the setCellFactory(Callback) method to set custom implementation for showing data.
     * Both comboboxes also use the setConverter(StringConverter) method to set custom implementations for switching between ZonedDateTime and String.
     * <p>
     * LAMBDA: In this method, lambdas are used in place of Callbacks when using setCellFactory. This is an intentional
     * choice to prevent having to deal with the complexity of the Callback interface. Because lambdas were used over Callbacks,
     * the resulting code is much easier to write, easier to read, and easier to understand. In simple cases like this, lambdas
     * are the better option.
     * </p>
     */
    public void setUpTimeBoxes() {
        // Set custom cell factories for time boxes
        cboxStartTime.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ZonedDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatZonedDateTime(item));
                }
            }
        });
        cboxStartTime.setConverter(new StringConverter<>() {
            @Override
            public String toString(ZonedDateTime zdt) {
                return formatZonedDateTime(zdt);
            }
            @Override
            public ZonedDateTime fromString(String s) {
                return null;
            }
        });
        cboxEndTime.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ZonedDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatZonedDateTime(item));
                }
            }
        });
        cboxEndTime.setConverter(new StringConverter<>() {
            @Override
            public String toString(ZonedDateTime zdt) {
                return formatZonedDateTime(zdt);
            }
            @Override
            public ZonedDateTime fromString(String s) {
                return null;
            }
        });
    }

}
