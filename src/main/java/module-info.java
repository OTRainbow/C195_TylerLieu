module com.tylerlieu.c195 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.tylerlieu.c195 to javafx.fxml;
    exports com.tylerlieu.c195;
    exports com.tylerlieu.c195.controller;
    opens com.tylerlieu.c195.controller to javafx.fxml;
}