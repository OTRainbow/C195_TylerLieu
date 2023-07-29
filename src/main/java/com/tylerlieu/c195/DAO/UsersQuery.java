package com.tylerlieu.c195.DAO;

import com.tylerlieu.c195.model.User;
import com.tylerlieu.c195.model.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class UsersQuery {
    public static ObservableList<User> getAllUsers() throws SQLException {
        String query = "SELECT User_ID as userID, User_Name as userName FROM users;";
        Statement statement = DB.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        ObservableList<User> usersList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            usersList.add(new User(
                resultSet.getInt("userID"),
                resultSet.getString("userName")
            ));
        }
        resultSet.close();
        statement.close();
        return usersList;
    }
    public static boolean tryLogin(String username, String password) throws SQLException, IOException {
        FileWriter fileWriter = new FileWriter( System.getProperty("user.dir") + "/login_activity.txt", true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        String query = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        PreparedStatement statement = DB.connection.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Session.user = new User(resultSet.getInt("User_ID"), resultSet.getString("User_Name"));
            System.out.println("Login successful.");
            bufferedWriter.write("\nSUCCESS," +
                    ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")) +
                    "," + username);
            resultSet.close();
            statement.close();
            bufferedWriter.close();
            return true;
        } else {
            System.out.println("Login failed.");
            bufferedWriter.write("\nFAILURE," +
                    ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")) +
                    "," + username);
            resultSet.close();
            statement.close();
            bufferedWriter.close();
            return false;
        }
    }
}
