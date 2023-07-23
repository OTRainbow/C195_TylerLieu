package com.tylerlieu.c195.DAO;

import com.tylerlieu.c195.model.CustomerItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomersQuery {
    public static ObservableList<CustomerItem> getAllCustomerRecords() throws SQLException {
        String query = "" +
                "SELECT " +
                    "cus.Customer_ID as customerID, " +
                    "cus.Customer_Name as name, " +
                    "cus.Phone as phone, " +
                    "cus.Address as address, " +
                    "fld.Division as division, " +
                    "cou.Country as country, " +
                    "cus.Postal_Code as postal " +
                "FROM customers as cus " +
                    "INNER JOIN first_level_divisions as fld " +
                    "ON cus.Division_ID = fld.Division_ID " +
                    "INNER JOIN countries as cou " +
                    "ON fld.Country_ID = cou.Country_ID;";
        Statement statement = DB.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        ObservableList<CustomerItem> customersList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            customersList.add(new CustomerItem(
                    resultSet.getInt("customerID"),
                    resultSet.getString("name"),
                    resultSet.getString("phone"),
                    resultSet.getString("address"),
                    resultSet.getString("division"),
                    resultSet.getString("country"),
                    resultSet.getString("postal")
            ));
        }
        return customersList;
    }
}
