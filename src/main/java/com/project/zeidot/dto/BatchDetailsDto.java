package com.project.zeidot.dto;

public class BatchDetailsDto {
    private String batchId;
    private String foodID;

    public BatchDetailsDto(String foodID , String batchId) {
        this.batchId = batchId;
        this.foodID = foodID;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public String getBatchId() {
        return batchId;
    }

    public String getFoodID() {
        return foodID;
    }
}
