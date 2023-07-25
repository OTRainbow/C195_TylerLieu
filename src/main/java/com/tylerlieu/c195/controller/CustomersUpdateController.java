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

public class CustomersUpdateController implements Initializable {
    public TextField fldID;
    public TextField fldName;
    public TextField fldPhone;
    public TextField fldAddress;
    public TextField fldPostal;
    public ComboBox<Country> cboxCountry;
    public Label lblDivision;
    public ComboBox<Division> cboxDivision;
    public Button btnUpdate;
    public Button btnCancel;
    public ObservableList<Country> countriesList;
    public ObservableList<Division> divisionsList;
    public Alert blankFields;
    public Alert attemptFailed;
    public Customer oldCustomer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        oldCustomer = CustomersController.selectedCustomer;

        blankFields = new Alert(Alert.AlertType.ERROR);
        blankFields.setContentText("One or more items have been left blank. Please fill in all fields.");
        attemptFailed = new Alert(Alert.AlertType.ERROR);
        attemptFailed.setContentText("Failed to update customer in database");

        try {
            countriesList = CountriesQuery.getAllCountries();
            divisionsList = DivisionsQuery.getAllDivisions();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Populate the Country combobox
        cboxCountry.setItems(countriesList);
        // Populate the Divisions combobox (Reused LAMBDA)
        FilteredList<Division> filteredDivisionList = new FilteredList<>(divisionsList, division -> division.getCountryID() == oldCustomer.getCountryID());
        cboxDivision.setItems(filteredDivisionList);

        // Fill in form
        fldID.setText(Integer.toString(oldCustomer.getCustomerID()));
        fldName.setText(oldCustomer.getName());
        fldPhone.setText(oldCustomer.getPhone());
        fldAddress.setText(oldCustomer.getAddress());
        fldPostal.setText(oldCustomer.getPostal());
        cboxCountry.setValue(oldCustomer.getCountry());
        cboxDivision.setValue(oldCustomer.getDivision());
    }

    public void onButtonUpdateClick(ActionEvent actionEvent) {
        if (fldName.getText().isEmpty() ||
                fldPhone.getText().isEmpty() ||
                fldAddress.getText().isEmpty() ||
                fldPostal.getText().isEmpty() ||
                cboxCountry.getValue() == null ||
                cboxDivision.getValue() == null) {
            blankFields.showAndWait();
            return;
        }
        Customer updatedCustomer = new Customer(
            oldCustomer.getCustomerID(), // re-using the same customerID
            fldName.getText(),
            fldAddress.getText(),
            fldPostal.getText(),
            fldPhone.getText(),
            ((Division)cboxDivision.getValue()).getDivisionID()
        );
        try {
            CustomersQuery.updateCustomer(updatedCustomer);
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
        cboxDivision.valueProperty().set(null);

        Country selectedCountry = (Country) cboxCountry.getValue();
        int selectedCountryID = selectedCountry.getCountryID();
        // LAMBDA EXPRESSION
        FilteredList<Division> filteredDivisionList = new FilteredList<>(divisionsList, division -> division.getCountryID() == selectedCountryID);
        cboxDivision.setItems(filteredDivisionList);
    }
}
