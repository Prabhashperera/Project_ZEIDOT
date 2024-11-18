package com.project.zeidot.controller.foodBank;

import com.project.zeidot.dto.FoodBankDto;
import com.project.zeidot.dto.FoodBatchDto;
import com.project.zeidot.model.foodBank.FoodBankModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FoodBankController implements Initializable {
    public TextField FBEmailTF;
    public TextField FBName;
    public TextField FBAddressTF;
    public Label FBDonationID;
    @FXML
    private TableColumn<FoodBankDto ,String> donationID;
    @FXML
    private TableColumn<FoodBankDto ,String> FBKName;
    @FXML
    private TableColumn<FoodBankDto ,String> FBKAddress;
    @FXML
    private TableColumn<FoodBankDto ,String> FBKEmail;
    @FXML
    private TableView<FoodBankDto> tableView;
    FoodBankModel fBKModel = new FoodBankModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        donationID.setCellValueFactory(new PropertyValueFactory<>("donationID"));
        FBKName.setCellValueFactory(new PropertyValueFactory<>("FBKName"));
        FBKAddress.setCellValueFactory(new PropertyValueFactory<>("FBKAddress"));
        FBKEmail.setCellValueFactory(new PropertyValueFactory<>("FBKEmail"));

        try {
            refreshPage();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshPage() throws SQLException {
        loadTable();
        String nextID = fBKModel.getNextFoodBankId();
        FBDonationID.setText(nextID);
        FBEmailTF.setText("");
        FBName.setText("");
        FBAddressTF.setText("");
    }

    public void saveBtnOnAction(ActionEvent event) {
        try{
            String donationId = FBDonationID.getText();
            String FBName = FBKName.getText();
            String FBAddress = FBAddressTF.getText();
            String FBEmail = FBEmailTF.getText();
            FoodBankDto dto = new FoodBankDto(donationId,FBName,FBAddress,FBEmail);
            boolean isSaved = fBKModel.saveFoodBank(dto);
            if(isSaved){
                new Alert(Alert.AlertType.CONFIRMATION , "Saved!!").show();
                refreshPage();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void editOnAction(ActionEvent event) {
    }
    public void deleteOnAction(ActionEvent event) {
    }

    //Table Methods
    public void loadTable() throws SQLException {
        // Convert ArrayList to ObservableList
        ArrayList<FoodBankDto> foodBankDetails =  fBKModel.getFoodBankDetails();
        ObservableList<FoodBankDto> observableList = FXCollections.observableArrayList(foodBankDetails);
        // Set the ObservableList to the TableView
        tableView.setItems(observableList);
    }

}