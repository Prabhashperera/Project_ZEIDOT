package com.project.zeidot.controller;

import com.project.zeidot.db.DBConnection;
import com.project.zeidot.dto.BatchDetailsDto;
import com.project.zeidot.dto.FoodBatchDto;
import com.project.zeidot.dto.FoodDto;
import com.project.zeidot.model.FoodBatchModel;
import com.project.zeidot.model.FoodManageModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FoodManageController implements Initializable {
    public AnchorPane mainAnchor;
    public Button detailsBtn;
    public TextField foodNameTF;
    public TextField foodWeightTF;
    public Label foodIDTF;
    public MenuButton menuButton;
    public Label date;
    public Label batchID;
    @FXML
    private Label currentTimeLabel;
    LocalDateTime currentDateTime = LocalDateTime.now(); // this for database saving

    //Food Table
    @FXML
    private TableColumn<FoodDto, String> idColumn;
    @FXML
    private TableColumn<FoodDto, String> nameColumn;
    @FXML
    private TableColumn<FoodDto, String> weightColumn;
    @FXML
    private TableColumn<FoodDto, String> expireDateColumn;
    @FXML
    private TableView<FoodDto> tableView;
    //Food Table
    //Batch Table
    @FXML
    private TableColumn<FoodBatchDto, String> FBId;
    @FXML
    private TableColumn<FoodBatchDto, String> amount;
    @FXML
    private TableColumn<FoodBatchDto, String> addedDate;
    @FXML
    private TableView<FoodBatchDto> foodBatchTable;
    // Batch Table
    FoodManageModel foodManageModel = new FoodManageModel();
    FoodBatchModel foodBatchModel = new FoodBatchModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainAnchor.getStyleClass().add("main-pane");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("foodID"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("foodName"));
        expireDateColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        currentTimeLabel.setText(currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        //batch table
        FBId.setCellValueFactory(new PropertyValueFactory<>("foodBatchId"));
        amount.setCellValueFactory(new PropertyValueFactory<>("foodAmount"));
        addedDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        try {
            batchID.setText(foodBatchModel.getCurrentBatchID());
            realtimeThread();
            refreshPage();
            loadTableData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveBtnOnAction(ActionEvent event) {
        try {
//            String batchID = foodBatchModel.getNextBatchId();
            String foodID = foodIDTF.getText();
            String foodName = foodNameTF.getText();
            String foodWeight = foodWeightTF.getText();
            LocalDateTime newDateTime = currentDateTime.plusHours(Long.parseLong(menuButton.getText()));
            String foodDuration = newDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            FoodDto dto = new FoodDto(foodID, foodName, foodWeight, foodDuration);
            boolean isSaved = foodManageModel.saveFood(dto);
            if (isSaved) {
//                foodManageModel.batchDetailsAdd(foodID , batchID);
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Saved", ButtonType.OK).show();
                BatchDetailsDto detailsDto = new BatchDetailsDto(foodID , batchID.getText());
                boolean isAdded = foodBatchModel.setBatchDetailsValues(detailsDto);
                if (isAdded) {
                    System.out.println("add una");
                    amountUpdate(foodWeight); // batch values tika add una gaman amount ekath wadi wenawa
                    refreshPage();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK).show();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void refreshPage() throws SQLException {
        loadNextCustomerId();
        currentDate();
        loadTableData();
        loadFoodBatchTable();
        foodNameTF.setText("");
        foodWeightTF.setText("");
        menuButton.setText("Hour");
    }

    public void loadNextCustomerId() throws SQLException {
        String nextFoodId = foodManageModel.getNextCustomerId();
        System.out.println(nextFoodId);
        foodIDTF.setText(nextFoodId);
    }

    public void setHour(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        menuButton.setText(menuItem.getText());
        System.out.println(menuButton.getText());
    }

    private void loadTableData() throws SQLException {
        ArrayList<FoodDto> customerDTOS = foodManageModel.getAllCustomers();

        ObservableList<FoodDto> FoodDTOS = FXCollections.observableArrayList();

        for (FoodDto foodDto : customerDTOS) {
            FoodDto foodTM = new FoodDto(
                    foodDto.getFoodID(),
                    foodDto.getWeight(),
                    foodDto.getFoodName(),
                    foodDto.getDuration()
            );
            FoodDTOS.add(foodTM);
        }

        tableView.setItems(FoodDTOS);
    }

    public void loadFoodBatchTable() throws SQLException {
        try {
            ArrayList<FoodBatchDto> batchDTOS = foodBatchModel.getAllBatchDetails(); // Renamed to batchDTOS

            ObservableList<FoodBatchDto> observableBatchDTOS = FXCollections.observableArrayList();

            for (FoodBatchDto batchDto : batchDTOS) {
                FoodBatchDto batchTM = new FoodBatchDto(
                        batchDto.getFoodBatchId(),
                        batchDto.getFoodAmount(),
                        batchDto.getDate()
                );
                observableBatchDTOS.add(batchTM);
            }
            foodBatchTable.setItems(observableBatchDTOS);
        }catch (Exception e) {
            e.getMessage();
        }
    }

    //Delete On Action
    public void deleteOnAction(ActionEvent event) {
        try {
            FoodDto food = tableView.getSelectionModel().getSelectedItem();
            String foodName = food.getFoodName();
            boolean isDeleted = foodManageModel.deleteFood(foodName);
            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Deleted", ButtonType.OK).show();
                //After Deleting a Food emediately calling Decrease amount of the food batch
                boolean isSaved = foodManageModel.decreaseAmount(foodManageModel.getCurrentWeight(batchID.getText()), Double.parseDouble(foodWeightTF.getText()));
                if (isSaved) {
                    System.out.println("Okooooooooooooooooma hari");
                    refreshPage();
                }
                refreshPage();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void editOnAction(ActionEvent event) {
        try {
            String foodID = foodIDTF.getText();
            String foodName = foodNameTF.getText();
            String foodWeight = foodWeightTF.getText();
            String foodDuration = menuButton.getText();
            FoodDto dto = new FoodDto(foodID, foodName, foodWeight, foodDuration);
            boolean isUpdated = foodManageModel.updateFood(dto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Updated", ButtonType.OK).show();
                refreshPage();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void tableOnAction(MouseEvent tableViewSortEvent) {
        FoodDto selectedItem = tableView.getSelectionModel().getSelectedItem();
        foodNameTF.setText(selectedItem.getFoodName());
        foodIDTF.setText(selectedItem.getFoodID());
        foodWeightTF.setText(selectedItem.getWeight());
        menuButton.setText(selectedItem.getDuration());
    }

    public void currentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = currentDate.format(formatter);
        date.setText(formattedDate);
    }

    //RealTime Clock With Threads
    public void realtimeThread() {
        Thread clockThread = new Thread(() -> {
            while (true) {
                try {
                    updateClock(); // Update the clock
                    Thread.sleep(1000); // Sleep for 1 second
                } catch (InterruptedException e) {
                    // Handle interruption
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        clockThread.setDaemon(true); // Set the thread as a daemon thread
        clockThread.start();
    }

    private void updateClock() {
        // Get the current time
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // Create a formatter
        // Update the label in the JavaFX Application Thread
        Platform.runLater(() -> {
            currentTimeLabel.setText(currentTime.format(formatter)); // Update the label with formatted time
        });
    }

    public void newBatchOnAction(ActionEvent event) {
        try{
            String FbatchID = foodBatchModel.getCurrentBatchID();
            String nextBatchID = foodBatchModel.getNextBatchId();
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            FoodBatchDto foodBatchDto = new FoodBatchDto(FbatchID , "0" , currentDate);
            boolean isSaved = Boolean.parseBoolean(foodBatchModel.setBatchValues(foodBatchDto));
            System.out.println(isSaved ? "save una" : "unnaha");
            batchID.setText(nextBatchID);
        }catch (NullPointerException | SQLException e) {
            e.getMessage();
        }
    }

    public void batchDetailsOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/view/homePage/foodBatchDetails.fxml"));
        stage.setResizable(false);
        Scene scene = new Scene(root , 1200 , 750);
        scene.getStylesheets().add(getClass().getResource("/view/Css/mainCss.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void amountUpdate(String foodWeight) throws SQLException {
        double weight = foodManageModel.getCurrentWeight(batchID.getText());
        boolean isUpdated = foodManageModel.updateAmount(weight , Double.parseDouble(foodWeight));
        if (isUpdated) {
            System.out.println("okkoma Hari");
        }
    } //Food ekak add weddi eke weight eka akthu wenaw
                                                                          //food amount ekata food Batch eke
}
