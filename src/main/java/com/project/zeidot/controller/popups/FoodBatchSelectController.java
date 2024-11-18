package com.project.zeidot.controller.popups;

import com.project.zeidot.controller.DonationController;
import com.project.zeidot.dto.FoodBatchDetailsDto;
import com.project.zeidot.dto.FoodBatchDto;
import com.project.zeidot.model.FoodBatchDetailsModel;
import com.project.zeidot.model.popups.FoodBatchSelectModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FoodBatchSelectController implements Initializable {
    @FXML
    private TableView<FoodBatchDto> tableView;
    @FXML
    private TableColumn<FoodBatchDto, String> FBID;
    @FXML
    private TableColumn<FoodBatchDto, String> FoodAmount;
    @FXML
    private TableColumn<FoodBatchDto, String> FBDate;
    @FXML
    private TableColumn<FoodBatchDto, String> isAvailable;
    @FXML
    private TableColumn<FoodBatchDto , String>  duration;
    FoodBatchSelectModel foodBatchSelectModel = new FoodBatchSelectModel(); //Model

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FBID.setCellValueFactory(new PropertyValueFactory<>("foodBatchId"));
        FoodAmount.setCellValueFactory(new PropertyValueFactory<>("foodAmount"));
        FBDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        isAvailable.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        try {
            loadTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadTable() throws SQLException {
        // Convert ArrayList to ObservableList
        ArrayList<FoodBatchDto> foodBatchDetails =  foodBatchSelectModel.getFoodBatchDetails();
        ObservableList<FoodBatchDto> observableList = FXCollections.observableArrayList(foodBatchDetails);

        // Set the ObservableList to the TableView
        tableView.setItems(observableList);
    }
 
    public String getFoodBatchID() {
        FoodBatchDto foodBatchDto = tableView.getSelectionModel().getSelectedItem();
        return foodBatchDto.getFoodBatchId();
    }//returns Table view selected FOODBatchID

    private DonationController donationController; // Instance of Controller
    public void setDonationController(DonationController donationController) {
        this.donationController = donationController;
    }//Initialize the Controller with Instance

    public void selectBtnOnAction() throws IOException {
        try{
            if (donationController != null) {
                String selectedBatchID = getFoodBatchID();
                donationController.batchIDInit(selectedBatchID);
            }else {
                new Alert(Alert.AlertType.ERROR , "Please Select a Batch!!").show();
            }
        }catch (Exception e){
            e.getMessage();
        }
    }

}
