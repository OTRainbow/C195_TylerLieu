package com.tylerlieu.c195;

import com.tylerlieu.c195.DAO.DB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

/**
 * Here is the entry point for the application.
 * @author Tyler Lieu
 */
public class Main extends Application {

    /**
     * Opens up the database connection upon starting the application
     */
    @Override
    public void init() {
        DB.openConnection();
    }

    /**
     * Loads the login page once the application has been started.
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Closes the database connection upon stopping the application
     */
    @Override
    public void stop() {
        DB.closeConnection();
    }

    /**
     * Main method for launching the application.
     * @param args
     */
    public static void main(String[] args) {
        //Locale.setDefault(new Locale("fr"));
        launch();
    }
}