package com.tylerlieu.c195.DAO;

import com.tylerlieu.c195.model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Static class for methods used to perform queries on the first_level_divisions table
 */
public class DivisionsQuery {
    /**
     * Retrieves all Divisions from the database.
     * @return An ObservableList of Division objects
     * @throws SQLException
     */
    public static ObservableList<Division> getAllDivisions() throws SQLException {
        String query = "SELECT Division_ID, Division, Country_ID FROM first_level_divisions;";
        Statement statement = DB.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        ObservableList<Division> divisionsList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            divisionsList.add(new Division(
                    resultSet.getInt("Division_ID"),
                    resultSet.getString("Division"),
                    resultSet.getInt("Country_ID")
            ));
        }
        return divisionsList;
    }
}
