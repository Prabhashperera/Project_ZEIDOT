package com.project.zeidot.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {
    public AnchorPane middlePane;
    public Button foodManageBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        middlePane.getChildren().clear();
        AnchorPane load = null;
        try {
            load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/homePage/homePage.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        middlePane.getChildren().add(load);
    }

    public void foodManageOnAction(ActionEvent event) throws IOException {
        middlePane.getChildren().clear();
        AnchorPane load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/homePage/foodManage.fxml")));
        middlePane.getChildren().add(load);
    }
}
