package com.tylerlieu.c195.DAO;

import com.tylerlieu.c195.model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DivisionsQuery {
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
