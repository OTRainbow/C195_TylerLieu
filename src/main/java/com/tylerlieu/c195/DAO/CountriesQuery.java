package com.tylerlieu.c195.DAO;

import com.tylerlieu.c195.model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Static class for methods used to perform queries on the countries table
 */
public class CountriesQuery {
    /**
     * Retrieves all Countries from the database.
     * @return An ObservableList of Country objects
     * @throws SQLException
     */
    public static ObservableList<Country> getAllCountries() throws SQLException {
        String query = "SELECT Country_ID, Country FROM countries;";
        Statement statement = DB.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        ObservableList<Country> countriesList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            countriesList.add(new Country(
                    resultSet.getInt("Country_ID"),
                    resultSet.getString("Country")
            ));
        }
        return countriesList;
    }
}
