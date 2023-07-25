package com.tylerlieu.c195.controller;

import com.tylerlieu.c195.DAO.CountriesQuery;
import com.tylerlieu.c195.DAO.CustomersQuery;
import com.tylerlieu.c195.DAO.DivisionsQuery;
import com.tylerlieu.c195.model.Country;
import com.tylerlieu.c195.model.Customer;
import com.tylerlieu.c195.model.Division;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomersAddController implements Initializable {
    public TextField fldID;
    public TextField fldName;
    public TextField fldPhone;
    public TextField fldAddress;
    public TextField fldPostal;
    public ComboBox<Country> cboxCountry;
    public Label lblDivision;
    public ComboBox<Division> cboxDivision;
    public Button btnAdd;
    public Button btnCancel;
    public ObservableList<Country> countriesList;
    public ObservableList<Division> divisionsList;
    public Alert blankFields;
    public Alert attemptFailed;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        blankFields = new Alert(Alert.AlertType.ERROR);
        blankFields.setContentText("One or more items have been left blank. Please fill in all fields.");
        attemptFailed = new Alert(Alert.AlertType.ERROR);
        attemptFailed.setContentText("Failed to add customer in database");

        try {
            countriesList = CountriesQuery.getAllCountries();
            divisionsList = DivisionsQuery.getAllDivisions();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Populate the Country combobox
        cboxCountry.setItems(countriesList);
    }
    public void onButtonAddClick(ActionEvent actionEvent) {
        if (fldName.getText().isEmpty() ||
            fldPhone.getText().isEmpty() ||
            fldAddress.getText().isEmpty() ||
            fldPostal.getText().isEmpty() ||
            cboxCountry.getValue() == null ||
            cboxDivision.getValue() == null) {
            blankFields.showAndWait();
            return;
        }
        Customer customer = new Customer(
            fldName.getText(),
            fldAddress.getText(),
            fldPostal.getText(),
            fldPhone.getText(),
            ((Division)cboxDivision.getValue()).getDivisionID()
        );
        try {
            CustomersQuery.addCustomer(customer);
        } catch (SQLException e) {
            attemptFailed.showAndWait();
            throw new RuntimeException(e);
        }
        CustomersController.loadTable();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void onButtonCancelClick(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void onCountrySelect(ActionEvent actionEvent) {
        lblDivision.setDisable(false);
        cboxDivision.setDisable(false);
        cboxDivision.valueProperty().set(null);

        Country selectedCountry = (Country) cboxCountry.getValue();
        int selectedCountryID = selectedCountry.getCountryID();
        // LAMBDA EXPRESSION
        FilteredList<Division> filteredDivisionList = new FilteredList<>(divisionsList, division -> division.getCountryID() == selectedCountryID);
        cboxDivision.setItems(filteredDivisionList);
    }
}
