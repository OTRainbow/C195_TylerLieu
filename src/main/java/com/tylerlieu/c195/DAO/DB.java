package com.tylerlieu.c195.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/client_schedule";
    private static final String DB_USER = "sqlUser";
    private static final String DB_PASSWORD = "Passw0rd!";
    public static Connection connection = null;

    /**
     * Opens the database connection
     */
    public static void openConnection() {
        // Precondition
        if (connection != null) { return; }
        // ------------
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("DB Connection Opened Successfully");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the database connection
     */
    public static void closeConnection() {
        // Precondition
        if (connection == null) { return; }
        // ------------
        try {
            connection.close();
            connection = null;
            System.out.println("DB Connection Closed Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
