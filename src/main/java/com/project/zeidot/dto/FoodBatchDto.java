package com.project.zeidot.dto;

public class FoodBatchDto {
    private String foodBatchId;
    private String foodAmount;
    private String date;
    private String isAvailable;

    public FoodBatchDto() {

    }

    public String getFoodBatchId() {
        return foodBatchId;
    }

    public void setFoodBatchId(String foodBatchId) {
        this.foodBatchId = foodBatchId;
    }

    public String getFoodAmount() {
        return foodAmount;
    }

    public FoodBatchDto(String foodBatchId, String foodAmount, String date , String isAvailable) {
        this.foodBatchId = foodBatchId;
        this.foodAmount = foodAmount;
        this.date = date;
        this.isAvailable = isAvailable;
    }

    public void setFoodAmount(String foodAmount) {
        this.foodAmount = foodAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }
}
