package com.tylerlieu.c195.controller;

import com.tylerlieu.c195.DAO.AppointmentsQuery;
import com.tylerlieu.c195.DAO.CustomersQuery;
import com.tylerlieu.c195.Main;
import com.tylerlieu.c195.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Optional;
import java.util.ResourceBundle;

/** This is the controller for managing the customersTab.fxml view. */
public class CustomersController implements Initializable {
    /**
     * A table that shows a list of all customers.
     */
    public TableView<Customer> customersTable;
    /**
     * A column for customersTable showing the customerID
     */
    public TableColumn<Customer, Integer> idColumn;
    /**
     * A column for customersTable showing the Customer's name
     */
    public TableColumn<Customer, String> nameColumn;
    /**
     * A column for customersTable showing the Customer;s phone number
     */
    public TableColumn<Customer, String> phoneColumn;
    /**
     * A column for customersTable showing the Customer's address
     */
    public TableColumn<Customer, String> addressColumn;
    /**
     * A column for customersTable showing the Customer's divison
     */
    public TableColumn<Customer, String> divisionColumn;
    /**
     * A column for customersTable showing the Customer's country
     */
    public TableColumn<Customer, String> countryColumn;
    /**
     * A column for customersTable showing the Customer's postal/zip code
     */
    public TableColumn<Customer, String> postalColumn;
    /**
     * A list of Customer objects to populate customersTable
     */
    public static ObservableList<Customer> customersList = FXCollections.observableArrayList();
    /**
     * The Customer that is currently selected from customersTable
     */
    public static Customer selectedCustomer;
    /**
     * An alert to show when no Customer has been selected
     */
    public Alert noSelectionError;
    /**
     * An alert to show when a customer fails to be deleted from the database
     */
    public Alert attemptFailed;
    /**
     * An alert to confirm if the user really wants to delete a customer
     */
    public Alert confirmDelete;
    /**
     * An alert to verify to the user that a customer has been deleted
     */
    public Alert doneDelete;

    /**
     * This method is called as soon as the view begins to load.
     * First, it sets the Alerts. Then, it sets up the columns.
     * Then, it loads the customersTable.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set Alerts
        noSelectionError = new Alert(Alert.AlertType.ERROR);
        noSelectionError.setContentText("Please select a customer");
        attemptFailed = new Alert(Alert.AlertType.ERROR);
        attemptFailed.setContentText("Failed to delete customer in database");
        confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDelete.setContentText("Deleting a customer will also delete all appointments associated with the customer. This cannot be undone. Are you sure you want to delete the selected customer?");
        doneDelete = new Alert(Alert.AlertType.INFORMATION);
        doneDelete.setContentText("Customer has been deleted from the database.");

        // Set Up Columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        postalColumn.setCellValueFactory(new PropertyValueFactory<>("postal"));

        // Populate the customersTable
        customersTable.setItems(customersList);
        loadTable();
    }

    /**
     * This method queries the customers table and populates the TableView customersTable
     */
    public static void loadTable() {
        try {
            customersList.clear();
            customersList.addAll(CustomersQuery.getAllCustomerRecords());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is called when users click on the add button.
     * It opens up the customersAddForm.fxml view.
     * @param actionEvent Used to get current stage
     */
    public void onButtonAddClick(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/customersAddForm.fxml"));
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
     *  This method is called when users click on the update button.
     *  It saves the selected Customer to selectedCustomer and opens up the customersUpdateForm.fxml view.
     * @param actionEvent Used to get current stage
     */
    public void onButtonUpdateClick(ActionEvent actionEvent) {
        selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            noSelectionError.showAndWait();
            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/customersUpdateForm.fxml"));
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
     * After confirming with the user, it will attempt to delete all appointments associated with the selected customer before eventually deleting the customer.
     * Notifies the user if the deletion is successful, and it reloads the tables.
     */
    public void onButtonDeleteClick() {
        selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            noSelectionError.showAndWait();
            return;
        }
        confirmDelete.setContentText("Deleting a customer will also delete all appointments associated with the customer. This cannot be undone. Are you sure you want to delete " + selectedCustomer.getName() + " from the database?");
        Optional<ButtonType> result = confirmDelete.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                // Delete all associated appointments
                AppointmentsQuery.deleteAppointmentsByCustomerID(selectedCustomer);
                // Delete the customer
                CustomersQuery.deleteCustomer(selectedCustomer);
                // Reload tables
                loadTable();
                AppointmentsController.loadTable();
                HomeController.loadAppointmentTypesTable();
                HomeController.loadContactScheduleTable(HomeController.selectedContact);
                HomeController.loadCustomerDivisionsTable();
                // Show Alert
                doneDelete.setContentText(selectedCustomer.getName() + " has been deleted from the database.");
                doneDelete.showAndWait();
            } catch (SQLException e) {
                attemptFailed.showAndWait();
                throw new RuntimeException(e);
            }
        }
    }
}
