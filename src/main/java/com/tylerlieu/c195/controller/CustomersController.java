package com.tylerlieu.c195.controller;

import com.tylerlieu.c195.DAO.CustomersQuery;
import com.tylerlieu.c195.model.CustomerItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {
    public TableView<CustomerItem> customersTable;
    public TableColumn<CustomerItem, Integer> idColumn;
    public TableColumn<CustomerItem, String> nameColumn;
    public TableColumn<CustomerItem, String> phoneColumn;
    public TableColumn<CustomerItem, String> addressColumn;
    public TableColumn<CustomerItem, String> divisionColumn;
    public TableColumn<CustomerItem, String> countryColumn;
    public TableColumn<CustomerItem, String> postalColumn;
    public ObservableList<CustomerItem> customersList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        postalColumn.setCellValueFactory(new PropertyValueFactory<>("postal"));
        customersTable.setItems(customersList);

        try {
            loadTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadTable() throws SQLException {
        customersList.clear();
        customersList.addAll(CustomersQuery.getAllCustomerRecords());
    }
}
