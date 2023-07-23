package com.tylerlieu.c195.DAO;

import com.tylerlieu.c195.model.User;
import com.tylerlieu.c195.model.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersQuery {
    public static boolean tryLogin(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        PreparedStatement statement = DB.connection.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Session.user = new User(resultSet.getInt("User_ID"), resultSet.getString("User_Name"));
            System.out.println("Login successful.");
            resultSet.close();
            statement.close();
            return true;
        } else {
            System.out.println("Login failed.");
            resultSet.close();
            statement.close();
            return false;
        }
    }
}
