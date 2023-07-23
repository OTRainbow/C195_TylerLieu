package com.tylerlieu.c195;

import com.tylerlieu.c195.DAO.DB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

public class Main extends Application {
    @Override
    public void init() {
        DB.openConnection();
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void stop() {
        DB.closeConnection();
    }
    public static void main(String[] args) {
        // Locale.setDefault(new Locale("fr"));
        launch();
    }
}