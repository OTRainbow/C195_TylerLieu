package com.tylerlieu.c195.controller;

import com.tylerlieu.c195.DAO.AppointmentsQuery;
import com.tylerlieu.c195.Main;
import com.tylerlieu.c195.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
/** This is the controller for managing the appointmentsTab.fxml view. */
public class AppointmentsController implements Initializable {
    /**
     * A table that shows a list of all appointments
     */
    public TableView<Appointment> appointmentsTable;
    /**
     * A column for appointmentsTable showing the appointmentID
     */
    public TableColumn<Appointment, Integer> idColumn;
    /**
     * A column for appointmentsTable showing appointment title
     */
    public TableColumn<Appointment, String> titleColumn;
    /**
     * A column for appointmentsTable showing appointment description
     */
    public TableColumn<Appointment, String> descriptionColumn;
    /**
     * A column for appointmentsTable showing appointment location
     */
    public TableColumn<Appointment, String> locationColumn;
    /**
     * A column for appointmentsTable showing contact name
     */
    public TableColumn<Appointment, String> contactNameColumn;
    /**
     * A column for appointmentsTable showing contact email
     */
    public TableColumn<Appointment, String> contactEmailColumn;
    /**
     * A column for appointmentsTable showing appointment type
     */
    public TableColumn<Appointment, String> typeColumn;
    /**
     * A column for appointmentsTable showing appointment start date/time
     */
    public TableColumn<Appointment, String> startColumn;
    /**
     * A column for appointmentsTable showing appointment end date/time
     */
    public TableColumn<Appointment, String> endColumn;
    /**
     * A column for appointmentsTable showing the customerID
     */
    public TableColumn<Appointment, Integer> customerIDColumn;
    /**
     * A column for appointmentsTable showing customer name
     */
    public TableColumn<Appointment, String> customerNameColumn;
    /**
     * A column for appointmentsTable showing the userID
     */
    public TableColumn<Appointment, Integer> userIDColumn;
    /**
     * A column for appointmentsTable showing username
     */
    public TableColumn<Appointment, String> userNameColumn;
    /**
     * A list of Appointment objects to populate appointmentsTable
     */
    public static ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
    /**
     * The Appointment that is currently selected from appointmentsTable
     */
    public static Appointment selectedAppointment;
    /**
     * An alert to show when no Appointment has been selected
     */
    public Alert noSelectionError;
    /**
     * An alert to show when an appointment fails to be deleted from the database
     */
    public Alert attemptFailed;
    /**
     * An alert to confirm if the user really wants to delete an appointment
     */
    public Alert confirmDelete;
    /**
     * An alert to verify to the user that a customer has been deleted
     */
    public Alert doneDelete;
    /**
     * A FilteredList with only appointments in the current month
     */
    public static FilteredList<Appointment> appointmentsMonthList;
    /**
     * A FilteredList with only appointments in the current week
     */
    public static FilteredList<Appointment> appointmentsWeekList;
    /**
     * A radiobutton that, when selected, switches to appointmentsWeekList
     */
    public RadioButton rbWeek;
    /**
     * A radiobutton that, when selected, switches to appointmentMonthlist
     */
    public RadioButton rbMonth;
    /**
     * A radiobutton that, when selected, switches to appointmentsList
     */
    public RadioButton rbAll;

    /**
     * This method is called as soon as the view begins to load.
     * First, it sets the Alerts. Then, it sets up the columns.
     * Then, it loads the appointmentsTable.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set Alerts
        noSelectionError = new Alert(Alert.AlertType.ERROR);
        noSelectionError.setContentText("Please select an appointment");
        attemptFailed = new Alert(Alert.AlertType.ERROR);
        attemptFailed.setContentText("Failed to delete appointment in database");
        confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDelete.setContentText("Are you sure you want to delete this appointment?");
        doneDelete = new Alert(Alert.AlertType.INFORMATION);
        doneDelete.setContentText("Appointment has been deleted from the database.");

        // Set Up Columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactNameColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        contactEmailColumn.setCellValueFactory(new PropertyValueFactory<>("contactEmail"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startDateLocalDisplay"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endDateLocalDisplay"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        // Populate the appointmentsTable
        appointmentsTable.setItems(appointmentsList);
        loadTable();
    }

    /**
     * This method queries the appointments table and populates the TableView appointmentsTable.
     * Then, it populates appointmentsMonthList and appointmentsWeekList by using lambdas.
     * <p>
     * LAMBDA: Lambda expressions are used in this method to create FilteredLists out of the appointmentsList.
     * The FilteredList constructor takes the appointmentsList as the first argument and a lambda expression as the second argument.
     * The result is a new FilteredList containing only the objects from the appointmentsList that match with the predicate.
     * Using lambda expressions this way prevents the developer from having to make additional SELECT queries to the database.
     * It also prevents the developer from having to write a loop, which is less convenient to write and less easy to read.
     * </p>
     */
    public static void loadTable() {
        try {
            appointmentsList.clear();
            appointmentsList.addAll(AppointmentsQuery.getAllAppointmentRecords());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ZonedDateTime today = ZonedDateTime.now();
        Month currentMonth = today.getMonth();
        ZonedDateTime beginWeek = today.minusDays(today.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
        ZonedDateTime endWeek = today.plusDays(DayOfWeek.SUNDAY.getValue() - today.getDayOfWeek().getValue());
        appointmentsMonthList = new FilteredList<>(appointmentsList, appointment -> appointment.getStartDateLocal().getMonth().equals(currentMonth));
        appointmentsWeekList = new FilteredList<>(appointmentsList, appointment -> !appointment.getStartDateLocal().isBefore(beginWeek) && !appointment.getStartDateLocal().isAfter(endWeek));
    }
    /**
     * This method is called when users click on the add button.
     * It opens up the appointmentsAddForm.fxml view.
     * @param actionEvent Used to get current stage
     */
    public void onButtonAddClick(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/appointmentsAddForm.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is called when users click on the update button.
     * It saves the selected Appointment to selectedAppintment and opens up the appointmentsUpdateForm.fxml view.
     * @param actionEvent Used to get current stage
     */
    public void onButtonUpdateClick(ActionEvent actionEvent) {
        selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            noSelectionError.showAndWait();
            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/appointmentsUpdateForm.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is called when users click on the delete button.
     * After confirming with the user, it will attempt to delete the selected appointment from the database.
     * It notifies the user if the deletion is successful, and it reloads the tables.
     */
    public void onButtonDeleteClick() {
        selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            noSelectionError.showAndWait();
            return;
        }
        confirmDelete.setContentText("Are you sure you want to delete appointment #" + selectedAppointment.getAppointmentID() + " for " + selectedAppointment.getType() + "?");
        Optional<ButtonType> result = confirmDelete.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                // Delete appointment
                AppointmentsQuery.deleteAppointment(selectedAppointment);
                // Reload tables
                loadTable();
                HomeController.loadAppointmentTypesTable();
                HomeController.loadContactScheduleTable(HomeController.selectedContact);
                HomeController.loadCustomerDivisionsTable();
                // Show Alert
                doneDelete.setContentText("Appointment #" + selectedAppointment.getAppointmentID() + " for " + selectedAppointment.getType() + " has been deleted from the database.");
                doneDelete.showAndWait();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This method is called when the user changes the selected radio button.
     * It assigns appointmentsTable to a different list depending on which radio button is selected.
     */
    public void onRadioButtonSwitch() {
        if (rbAll.isSelected()) {
            appointmentsTable.setItems(appointmentsList);
        } else if (rbMonth.isSelected()) {
            appointmentsTable.setItems(appointmentsMonthList);
        } else if (rbWeek.isSelected()) {
            appointmentsTable.setItems(appointmentsWeekList);
        } else {
            appointmentsTable.setItems(appointmentsList);
        }

    }
}
