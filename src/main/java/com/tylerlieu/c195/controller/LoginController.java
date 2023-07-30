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

/** This is the controller for managing the login.fxml view. */
public class LoginController implements Initializable {
    /**
     * A title label
     */
    public Label lblTitle;
    /**
     * A username label
     */
    public Label lblUsername;
    /**
     * A password label
     */
    public Label lblPassword;
    /**
     * A button to login
     */
    public Button btnLogin;
    /**
     * A location label
     */
    public Label lblLocation;
    /**
     * A button to exit
     */
    public Button btnExit;
    /**
     * A textfield for entering a username
     */
    public TextField fldUsername;
    /**
     * A passwordfield for entering a password
     */
    public PasswordField fldPassword;
    /**
     * An alert to show when a field has been left blank
     */
    public Alert blankFields;
    /**
     * An alert to show when the provided username and password inputs do not match any records
     */
    public Alert invalidLogin;
    /**
     * An alert to show when there is some kind of database error
     */
    public Alert sqlException;

    /**
     * This method is called as soon as the view begins to load.
     *
     * First, it sets the localization label to show the user's ZoneId.
     * Then, it figures out if the user's language is set to French and changes the text accordingly.
     *
     * @param url
     * @param resourceBundle
     */
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

    /**
     * This method is called whenever the user clicks on the login button.
     *
     * First, it retrieves the user's input. Then, if any of them are blank, it alerts the user and returns to the end of the method.
     * If not, the method attempts to log the user in. If successful, the scene changes to allow the user to view the application.
     * Otherwise, an alert will show and the user must try again.
     * @param actionEvent Used to get the current stage
     */
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

    /**
     * This method calls whenever the user clicks on the exit button.
     * This closes the application window, which stops the application.
     * @param actionEvent Used to get the current stage
     */
    public void onButtonExitClick(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
