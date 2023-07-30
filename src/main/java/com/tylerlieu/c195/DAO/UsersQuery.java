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

/**
 * Static class for methods used to perform queries on the users table
 */
public class UsersQuery {
    /**
     * Retrieves all Users from the database.
     * @return An ObservableList of User objects
     * @throws SQLException
     */
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

    /**
     * Attempts to log the user into the system by finding a record with a matching username and password combination.
     * REQUIREMENT C: Records each log-in attempt into login_activity.txt
     * @param username Input from user
     * @param password Input from user
     * @return Returns true if successful, false if unsuccessful
     * @throws SQLException
     * @throws IOException
     */
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
                    " UTC, User: " + username);
            resultSet.close();
            statement.close();
            bufferedWriter.close();
            return true;
        } else {
            System.out.println("Login failed.");
            bufferedWriter.write("\nFAILURE," +
                    ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")) +
                    " UTC, User: " + username);
            resultSet.close();
            statement.close();
            bufferedWriter.close();
            return false;
        }
    }
}
