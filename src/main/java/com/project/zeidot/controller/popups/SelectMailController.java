package com.project.zeidot.controller.popups;

import com.project.zeidot.controller.mail.SendMailController;
import com.project.zeidot.dto.DonationDto;
import com.project.zeidot.dto.FoodBankDto;
import com.project.zeidot.model.popups.SelectMailModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SelectMailController implements Initializable {
    @FXML
    private TableView<FoodBankDto> tableView;
    @FXML
    private TableColumn<FoodBankDto , String> FBName;
    @FXML
    private TableColumn<FoodBankDto , Integer> emailAddress;
    private SendMailController sendMailController; //Controller Instance
    SelectMailModel selectMailModel = new SelectMailModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FBName.setCellValueFactory(new PropertyValueFactory<>("FBKName"));
        emailAddress.setCellValueFactory(new PropertyValueFactory<>("FBKEmail"));

        try {
            loadTable();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setSendMailController(SendMailController sendMailController) {
        this.sendMailController = sendMailController;
    }

    public void loadTable() throws SQLException {
        // Convert ArrayList to ObservableList
        ArrayList<FoodBankDto> foodBankDetails = selectMailModel.getFoodBankDetails();
        ObservableList<FoodBankDto> observableList = FXCollections.observableArrayList(foodBankDetails);
        // Set the ObservableList to the TableView
        tableView.setItems(observableList);
    }

    public void selectEmailBtnOnAction(ActionEvent event) {
        FoodBankDto selectedItem = tableView.getSelectionModel().getSelectedItem();
        sendMailController.setMailToBtn(selectedItem.getFBKEmail());
    }
}
