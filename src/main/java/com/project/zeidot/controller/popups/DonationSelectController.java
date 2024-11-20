package com.project.zeidot.controller.popups;

import com.project.zeidot.controller.delivery.DeliveryController;
import com.project.zeidot.dto.DeliverDto;
import com.project.zeidot.dto.DonationDto;
import com.project.zeidot.model.popups.DonationSelectModel;
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

public class DonationSelectController implements Initializable {
    @FXML
    private TableView<DonationDto> tableView;
    @FXML
    private TableColumn<DonationDto, String> donationID;
    @FXML
    private TableColumn<DonationDto, String> donationName;
    @FXML
    private TableColumn<DonationDto, String> FBId;
    @FXML
    private TableColumn<DonationDto, String> foodBankID;
    private DeliveryController donationController;
    private DonationSelectModel donationModel = new DonationSelectModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        donationID.setCellValueFactory(new PropertyValueFactory<>("donationID"));
        donationName.setCellValueFactory(new PropertyValueFactory<>("donationName"));
        FBId.setCellValueFactory(new PropertyValueFactory<>("FBId"));
        foodBankID.setCellValueFactory(new PropertyValueFactory<>("foodBankID"));

        try {
            loadTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadTable() throws SQLException {
        // Convert ArrayList to ObservableList
        ArrayList<DonationDto> foodBankDetails = donationModel.getDeliveryDetails();
        ObservableList<DonationDto> observableList = FXCollections.observableArrayList(foodBankDetails);
        // Set the ObservableList to the TableView
        tableView.setItems(observableList);
    }

    public void setDonationController(DeliveryController donationController) {
        this.donationController = donationController;
    }

    public void getDonationID() {
        DonationDto selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            donationController.setDonationID(selectedItem.getDonationID());
        }
    }

    public void selectBtnOnAction(ActionEvent event) {
        getDonationID();
    }
}
