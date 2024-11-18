module com.project.zeidot {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.project.zeidot to javafx.fxml;
    opens com.project.zeidot.controller to javafx.fxml;
    opens com.project.zeidot.controller.popups to javafx.fxml;
    exports com.project.zeidot.dto;
    exports com.project.zeidot.controller;
    exports com.project.zeidot;
    opens com.project.zeidot.dto to javafx.base;
    exports com.project.zeidot.controller.popups;
    opens com.project.zeidot.controller.foodBank to javafx.fxml;
    exports com.project.zeidot.controller.foodBank;
}
