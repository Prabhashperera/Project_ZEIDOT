package com.project.zeidot.dto;

public class FoodBatchDto {
    private String foodBatchId;
    private String foodAmount;
    private String date;

    public String getFoodBatchId() {
        return foodBatchId;
    }

    public void setFoodBatchId(String foodBatchId) {
        this.foodBatchId = foodBatchId;
    }

    public String getFoodAmount() {
        return foodAmount;
    }

    public FoodBatchDto(String foodBatchId, String foodAmount, String date) {
        this.foodBatchId = foodBatchId;
        this.foodAmount = foodAmount;
        this.date = date;
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
}
