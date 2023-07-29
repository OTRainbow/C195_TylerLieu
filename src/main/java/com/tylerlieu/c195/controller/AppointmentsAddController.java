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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AppointmentsAddController implements Initializable {
    public TextField fldID;
    public TextField fldTitle;
    public TextArea fldDescription;
    public TextField fldLocation;
    public TextField fldType;
    public ComboBox<Contact> cboxContact;
    public ObservableList<Contact> contactsList;
    public ComboBox<Customer> cboxCustomer;
    public ObservableList<Customer> customersList;
    public ComboBox<User> cboxUser;
    public ObservableList<User> usersList;
    public DatePicker datePicker;
    public ComboBox<ZonedDateTime> cboxStartTime;
    public ComboBox<ZonedDateTime> cboxEndTime;
    public Button btnAdd;
    public Button btnCancel;
    public Label lblEndTime;
    public Alert blankFields;
    public Alert attemptFailed;
    public Alert overlapIssue;
    public Alert businessHours;
    public Label lblStartTime;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        blankFields = new Alert(Alert.AlertType.ERROR);
        blankFields.setContentText("One or more items have been left blank. Please fill in all fields.");
        attemptFailed = new Alert(Alert.AlertType.ERROR);
        attemptFailed.setContentText("Failed to add appointment in database");
        overlapIssue = new Alert(Alert.AlertType.ERROR);
        overlapIssue.setContentText("Unable to add appointment because it overlaps with an existing appointment");
        businessHours = new Alert(Alert.AlertType.ERROR);
        businessHours.setContentText("Unable to add appointment because it exceeds businesss hours (8AM to 10PM Eastern Time)");

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

        // Set custom cell factories for time boxes
        setUpTimeBoxes();
    }
    public void onButtonAddClick(ActionEvent actionEvent) {
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
        Appointment appointment = new Appointment(
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
        try {
            AppointmentsQuery.addAppointment(appointment);
        } catch (SQLException e) {
            attemptFailed.showAndWait();
            throw new RuntimeException(e);
        }
        AppointmentsController.loadTable();
        HomeController.loadAppointmentTypesTable();
        HomeController.loadContactScheduleTable(cboxContact.getValue());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void onButtonCancelClick(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
    public void onStartTimeSelect(ActionEvent actionEvent) {
        lblEndTime.setDisable(false);
        cboxEndTime.setDisable(false);
        cboxEndTime.valueProperty().set(null);
        if (cboxStartTime.getValue() == null) {
            return;
        }
        ObservableList<ZonedDateTime> endTimeSelectionList = FXCollections.observableArrayList();
        ZonedDateTime iTime = cboxStartTime.getValue().plusMinutes(5);
        ZonedDateTime lastTime = ZonedDateTime.of(datePicker.getValue(), LocalTime.of(22,0), ZoneId.of("America/New_York")).withZoneSameInstant(ZoneId.systemDefault());
        while (!iTime.isAfter(lastTime)) {
            endTimeSelectionList.add(iTime);
            iTime = iTime.plusMinutes(5);
        }
        cboxEndTime.setItems(endTimeSelectionList);
    }
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
    public Boolean doesThisOverlap() {
        for (Appointment existingAppointment : AppointmentsController.appointmentsList) {
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

    public void onDateSelected(ActionEvent actionEvent) {
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
    public String formatZonedDateTime(ZonedDateTime zdt) {
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h:mm a");
        return zdt.format(timeFormat);
    }

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
