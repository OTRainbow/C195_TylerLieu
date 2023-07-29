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

public class AppointmentsController implements Initializable {
    public TableView<Appointment> appointmentsTable;
    public TableColumn<Appointment, Integer> idColumn;
    public TableColumn<Appointment, String> titleColumn;
    public TableColumn<Appointment, String> descriptionColumn;
    public TableColumn<Appointment, String> locationColumn;
    public TableColumn<Appointment, String> contactNameColumn;
    public TableColumn<Appointment, String> contactEmailColumn;
    public TableColumn<Appointment, String> typeColumn;
    public TableColumn<Appointment, String> startColumn;
    public TableColumn<Appointment, String> endColumn;
    public TableColumn<Appointment, Integer> customerIDColumn;
    public TableColumn<Appointment, String> customerNameColumn;
    public TableColumn<Appointment, Integer> userIDColumn;
    public TableColumn<Appointment, String> userNameColumn;
    public static ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
    public Button btnAddAppointment;
    public Button btnUpdateAppointment;
    public Button btnDeleteAppointment;
    public static Appointment selectedAppointment;
    public Alert noSelectionError;
    public Alert attemptFailed;
    public Alert confirmDelete;
    public Alert doneDelete;
    public static FilteredList<Appointment> appointmentsMonthList;
    public static FilteredList<Appointment> appointmentsWeekList;
    public RadioButton rbWeek;
    public RadioButton rbMonth;
    public RadioButton rbAll;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        noSelectionError = new Alert(Alert.AlertType.ERROR);
        noSelectionError.setContentText("Please select an appointment");
        attemptFailed = new Alert(Alert.AlertType.ERROR);
        attemptFailed.setContentText("Failed to delete appointment in database");
        confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDelete.setContentText("Are you sure you want to delete this appointment?");
        doneDelete = new Alert(Alert.AlertType.INFORMATION);
        doneDelete.setContentText("Appointment has been deleted from the database.");


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
        appointmentsTable.setItems(appointmentsList);
        loadTable();
    }
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

    public void onButtonDeleteClick(ActionEvent actionEvent) {
        selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            noSelectionError.showAndWait();
            return;
        }
        confirmDelete.setContentText("Are you sure you want to cancel appointment " + selectedAppointment.getAppointmentID() + " : " + selectedAppointment.getType() + "?");
        Optional<ButtonType> result = confirmDelete.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                AppointmentsQuery.deleteAppointment(selectedAppointment);
                loadTable();
                doneDelete.setContentText("Appointment " + selectedAppointment.getAppointmentID() + " for " + selectedAppointment.getType() + " has been deleted from the database.");
                doneDelete.showAndWait();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void onRadioButtonSwitch(ActionEvent actionEvent) {
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
