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

/** This is the controller for managing the customersAddForm.fxml view. */
public class CustomersAddController implements Initializable {
    /**
     * A field for customerID
     */
    public TextField fldID;
    /**
     * A field for customer name
     */
    public TextField fldName;
    /**
     * A field for customer phone number
     */
    public TextField fldPhone;
    /**
     * A field for customer address
     */
    public TextField fldAddress;
    /**
     * A field for customer postal/zip code
     */
    public TextField fldPostal;
    /**
     * A ComboBox to select a Country
     */
    public ComboBox<Country> cboxCountry;
    /**
     * A label for Division
     */
    public Label lblDivision;
    /**
     * A ComboBox to select a Division
     */
    public ComboBox<Division> cboxDivision;
    /**
     * A list of Countries to populate cboxCountry
     */
    public ObservableList<Country> countriesList;
    /**
     * A list of Divisions to populate cboxDivision
     */
    public ObservableList<Division> divisionsList;
    /**
     * An Alert to show if the user leaves a field blank when they click add
     */
    public Alert blankFields;
    /**
     * An Alert to show if adding the customer in the database fails
     */
    public Alert attemptFailed;

    /**
     * This method is called as soon as the view begins to load.
     * First, it loads all the alerts.
     * Then, it populates countriesList and divisionsList.
     * Finally, cboxCountry loads the countriesList.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load Alerts
        blankFields = new Alert(Alert.AlertType.ERROR);
        blankFields.setContentText("One or more items have been left blank. Please fill in all fields.");
        attemptFailed = new Alert(Alert.AlertType.ERROR);
        attemptFailed.setContentText("Failed to add customer in database");

        // Populate Lists
        try {
            countriesList = CountriesQuery.getAllCountries();
            divisionsList = DivisionsQuery.getAllDivisions();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Populate the Country combobox
        cboxCountry.setItems(countriesList);
    }

    /**
     * This method is called when the user clicks on the add button.
     * First, it checks for blank fields.
     * Then, it creates a new Customer object.
     * Then, it attempts to run CustomersQuery.addCustomer(Customer).
     * If successful, the method reloads all the tables and closes the form stage.
     * @param actionEvent Used to get current stage
     */
    public void onButtonAddClick(ActionEvent actionEvent) {
        // Check for empty fields
        if (fldName.getText().isEmpty() ||
            fldPhone.getText().isEmpty() ||
            fldAddress.getText().isEmpty() ||
            fldPostal.getText().isEmpty() ||
            cboxCountry.getValue() == null ||
            cboxDivision.getValue() == null) {
            blankFields.showAndWait();
            return;
        }
        // Construct new customer
        Customer customer = new Customer(
            fldName.getText(),
            fldAddress.getText(),
            fldPostal.getText(),
            fldPhone.getText(),
            cboxDivision.getValue().getDivisionID()
        );
        // Add customer to database
        try {
            CustomersQuery.addCustomer(customer);
        } catch (SQLException e) {
            attemptFailed.showAndWait();
            throw new RuntimeException(e);
        }
        // Reload tables
        CustomersController.loadTable();
        HomeController.loadAppointmentTypesTable();
        HomeController.loadContactScheduleTable(HomeController.selectedContact);
        HomeController.loadCustomerDivisionsTable();
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
     * This method is called when the user makes a Country selection.
     * It resets cboxDivision and creates a new FilteredList of Divisions to repopulate cboxDivision.
     * <p>
     * LAMBDA: A lambda expression is used to create a FilteredList out of the divisionsList.
     * The FilteredList constructor takes the divisionsList as the first argument and a lambda expression as the second argument.
     * The result is a new FilteredList containing only the objects from divisionsList that match with the predicate.
     * The justification for using this lambda expression is that it allows the developer to achieve the requirement that
     * the divisions list displaying in the divisions combobox should be filtered by the Country selected. Fulfilling that
     * requirement only took one line of code. It was easy to write, and it is easier to read.
     *</p>
     */
    public void onCountrySelect() {
        // Enables for first time
        lblDivision.setDisable(false);
        cboxDivision.setDisable(false);
        // Deselect
        cboxDivision.valueProperty().set(null);

        Country selectedCountry = cboxCountry.getValue();
        int selectedCountryID = selectedCountry.getCountryID();
        // LAMBDA EXPRESSION
        FilteredList<Division> filteredDivisionList = new FilteredList<>(divisionsList, division -> division.getCountryID() == selectedCountryID);
        cboxDivision.setItems(filteredDivisionList);
    }
}
