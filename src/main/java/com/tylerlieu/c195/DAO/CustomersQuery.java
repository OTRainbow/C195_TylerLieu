package com.tylerlieu.c195.DAO;

import com.tylerlieu.c195.model.AppointmentTypeCount;
import com.tylerlieu.c195.model.Customer;
import com.tylerlieu.c195.model.CustomerDivisionCount;
import com.tylerlieu.c195.model.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomersQuery {
    public static ObservableList<Customer> getAllCustomerRecords() throws SQLException {
        String query = "" +
            "SELECT " +
                "cus.Customer_ID as customerID, " +
                "cus.Customer_Name as name, " +
                "cus.Phone as phone, " +
                "cus.Address as address, " +
                "cus.Division_ID as divisionID, " +
                "fld.Division as division, " +
                "fld.Country_ID as countryID, " +
                "cou.Country as country, " +
                "cus.Postal_Code as postal " +
            "FROM customers as cus " +
                "INNER JOIN first_level_divisions as fld " +
                "ON cus.Division_ID = fld.Division_ID " +
                "INNER JOIN countries as cou " +
                "ON fld.Country_ID = cou.Country_ID;";
        Statement statement = DB.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        ObservableList<Customer> customersList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            customersList.add(new Customer(
                resultSet.getInt("customerID"),
                resultSet.getString("name"),
                resultSet.getString("phone"),
                resultSet.getString("address"),
                resultSet.getInt("divisionID"),
                resultSet.getString("division"),
                resultSet.getInt("countryID"),
                resultSet.getString("country"),
                resultSet.getString("postal")
            ));
        }
        resultSet.close();
        statement.close();
        return customersList;
    }
    public static void addCustomer(Customer customer) throws SQLException {
        String query = "" +
            "INSERT INTO customers( " +
                "Customer_Name, " +
                "Address, " +
                "Postal_Code, " +
                "Phone, " +
                "Create_Date, " +
                "Created_By, " +
                "Last_Update, " +
                "Last_Updated_By, " +
                "Division_ID " +
                ") " +
            "VALUES ( ?, ?, ?, ?, NOW(), ?, NOW(), ?, ? );";
        PreparedStatement statement = DB.connection.prepareStatement(query);
        statement.setString(1, customer.getName());
        statement.setString(2, customer.getAddress());
        statement.setString(3, customer.getPostal());
        statement.setString(4, customer.getPhone());
        statement.setString(5, Session.user.getUserName());
        statement.setString(6, Session.user.getUserName());
        statement.setInt(7, customer.getDivisionID());
        statement.executeUpdate();
        statement.close();
    }

    public static void updateCustomer(Customer updatedCustomer) throws SQLException {
        String query = "" +
            "UPDATE customers " +
            "SET " +
                "Customer_Name = ?, " +
                "Address = ?, " +
                "Postal_Code = ?, " +
                "Phone = ?, " +
                "Last_Update = NOW(), " +
                "Last_Updated_By = ?, " +
                "Division_ID = ? " +
            "WHERE Customer_ID = ?;";
        PreparedStatement statement = DB.connection.prepareStatement(query);
        statement.setString(1, updatedCustomer.getName());
        statement.setString(2, updatedCustomer.getAddress());
        statement.setString(3, updatedCustomer.getPostal());
        statement.setString(4, updatedCustomer.getPhone());
        statement.setString(5, Session.user.getUserName());
        statement.setInt(6, updatedCustomer.getDivisionID());
        statement.setInt(7, updatedCustomer.getCustomerID());
        statement.executeUpdate();
        statement.close();
    }
    public static void deleteCustomer(Customer customer) throws SQLException {
        String query = "DELETE FROM customers WHERE Customer_ID = ?;";
        PreparedStatement statement = DB.connection.prepareStatement(query);
        statement.setInt(1, customer.getCustomerID());
        statement.executeUpdate();
        statement.close();
    }
    public static ObservableList<CustomerDivisionCount> getCustomerDivisionCountRecords() throws SQLException {
        String query = "" +
            "SELECT cou.Country, fld.Division, COUNT(*) " +
            "FROM customers as cus " +
            "INNER JOIN first_level_divisions as fld " +
            "ON cus.Division_ID = fld.Division_ID " +
            "INNER JOIN countries as cou " +
            "ON fld.Country_ID = cou.Country_ID " +
            "GROUP BY cou.Country, fld.Division;";
        Statement statement = DB.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        ObservableList<CustomerDivisionCount> customerDivisionsList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            customerDivisionsList.add(new CustomerDivisionCount(
                resultSet.getString("Country"),
                resultSet.getString("Division"),
                resultSet.getInt("COUNT(*)")
            ));
        }
        resultSet.close();
        statement.close();
        return customerDivisionsList;
    }
}
