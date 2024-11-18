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
        try {
            middlePane.getChildren().clear(); //When Loading to the main Layout Immediately Navigate into Home Page
            AnchorPane load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/homePage/homePage.fxml")));
            middlePane.getChildren().add(load);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void foodManageOnAction(ActionEvent event) throws IOException { //Food Manage Navigation
        middlePane.getChildren().clear();
        AnchorPane load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/homePage/foodManage.fxml")));
        middlePane.getChildren().add(load);
    }

    public void donationOnAction(ActionEvent event) throws IOException {
        middlePane.getChildren().clear();
        AnchorPane load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/homePage/donationPage.fxml")));
        middlePane.getChildren().add(load);
    }

    public void foodBankOnAction(ActionEvent event) throws IOException {
        middlePane.getChildren().clear();
        AnchorPane load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/homePage/foodBank/foodBank.fxml")));
        middlePane.getChildren().add(load);
    }
}