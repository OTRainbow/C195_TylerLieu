package com.tylerlieu.c195.controller;

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

public class CustomersController implements Initializable {
    public TableView<Customer> customersTable;
    public TableColumn<Customer, Integer> idColumn;
    public TableColumn<Customer, String> nameColumn;
    public TableColumn<Customer, String> phoneColumn;
    public TableColumn<Customer, String> addressColumn;
    public TableColumn<Customer, String> divisionColumn;
    public TableColumn<Customer, String> countryColumn;
    public TableColumn<Customer, String> postalColumn;
    public static ObservableList<Customer> customersList = FXCollections.observableArrayList();
    public Button btnAddCustomer;
    public Button btnUpdateCustomer;
    public Button btnDeleteCustomer;
    public static Customer selectedCustomer;
    public Alert noSelectionError;
    public Alert attemptFailed;
    public Alert confirmDelete;
    public Alert doneDelete;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        noSelectionError = new Alert(Alert.AlertType.ERROR);
        noSelectionError.setContentText("Please select a customer");
        attemptFailed = new Alert(Alert.AlertType.ERROR);
        attemptFailed.setContentText("Failed to delete customer in database");
        confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDelete.setContentText("Deleting a customer will also delete all appointments associated with the customer. This cannot be undone. Are you sure you want to delete the selected customer?");
        doneDelete = new Alert(Alert.AlertType.INFORMATION);
        doneDelete.setContentText("Customer has been deleted from the database.");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        postalColumn.setCellValueFactory(new PropertyValueFactory<>("postal"));
        customersTable.setItems(customersList);
        loadTable();
    }
    public static void loadTable() {
        try {
            customersList.clear();
            customersList.addAll(CustomersQuery.getAllCustomerRecords());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void onButtonDeleteClick(ActionEvent actionEvent) {
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

                //
                CustomersQuery.deleteCustomer(selectedCustomer);
                loadTable();
                doneDelete.setContentText(selectedCustomer.getName() + " has been deleted from the database.");
                doneDelete.showAndWait();
            } catch (SQLException e) {
                attemptFailed.showAndWait();
                throw new RuntimeException(e);
            }
        }
    }
}
