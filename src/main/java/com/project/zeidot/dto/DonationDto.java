package com.project.zeidot.dto;

public class DonationDto {
    private String donationID;
    private String donationName;
    private String foodBatchID;
    private String foodBankID;

    public DonationDto(String donationID, String donationName, String foodBatchID , String foodBankID) {
        this.donationID = donationID;
        this.donationName = donationName;
        this.foodBatchID = foodBatchID;
        this.foodBankID = foodBankID;
    }

    public String getFoodBankID() {
        return foodBankID;
    }

    public void setFoodBankID(String foodBankID) {
        this.foodBankID = foodBankID;
    }

    public String getDonationID() {
        return donationID;
    }

    public void setDonationID(String donationID) {
        this.donationID = donationID;
    }

    public String getDonationName() {
        return donationName;
    }

    public void setDonationName(String donationName) {
        this.donationName = donationName;
    }

    public String getFoodBatchID() {
        return foodBatchID;
    }

    public void setFoodBatchID(String foodBatchID) {
        this.foodBatchID = foodBatchID;
    }
}
