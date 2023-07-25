package com.tylerlieu.c195.controller;

import com.tylerlieu.c195.DAO.UsersQuery;
import com.tylerlieu.c195.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController  implements Initializable {

    public Label lblTitle;
    public Label lblUsername;
    public Label lblPassword;
    public Button btnLogin;
    public Label lblLocation;
    public Button btnExit;
    public TextField fldUsername;
    public PasswordField fldPassword;
    public Alert blankFields;
    public Alert invalidLogin;
    public Alert sqlException;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Displays user location/timezone
        lblLocation.setText(String.valueOf(ZoneId.systemDefault()));
        // Localization
        Locale userLocale = Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("Login", userLocale);
        if (userLocale.getLanguage().equals("en") || userLocale.getLanguage().equals("fr")) {
            lblTitle.setText(rb.getString("lblTitle"));
            lblUsername.setText(rb.getString("lblUsername"));
            lblPassword.setText(rb.getString("lblPassword"));
            btnLogin.setText(rb.getString("btnLogin"));
            btnExit.setText(rb.getString("btnExit"));

            blankFields = new Alert(Alert.AlertType.ERROR);
            invalidLogin = new Alert(Alert.AlertType.ERROR);
            sqlException = new Alert(Alert.AlertType.ERROR);
            blankFields.setContentText(rb.getString("blankFields"));
            invalidLogin.setContentText(rb.getString("invalidLogin"));
            sqlException.setContentText(rb.getString("sqlException"));
        }
    }

    public void onButtonLoginClick(ActionEvent actionEvent) {
        String username = fldUsername.getText();
        String password = fldPassword.getText();
        // Check for blank fields
        if (username.isEmpty() || password.isEmpty()) {
            blankFields.showAndWait();
            return;
        }
        // Attempt to log in
        try {
            if (UsersQuery.tryLogin(username, password)) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/main.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
            } else {
                invalidLogin.showAndWait();
            }
        }
        catch ( Exception e) {
            e.printStackTrace();
            sqlException.showAndWait();
        }
    }

    public void onButtonExitClick(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
