package com.project.zeidot.dto;

public class FoodBankDto {
    private String donationID;
    private String FBKName;
    private String FBKAddress;
    private String FBKEmail;

    public FoodBankDto(String donationID, String FBKName, String FBKAddress, String FBKEmail) {
        this.donationID = donationID;
        this.FBKName = FBKName;
        this.FBKAddress = FBKAddress;
        this.FBKEmail = FBKEmail;
    }

    public FoodBankDto() {

    }

    public String getDonationID() {
        return donationID;
    }

    public void setDonationID(String donationID) {
        this.donationID = donationID;
    }

    public String getFBKName() {
        return FBKName;
    }

    public void setFBKName(String FBKName) {
        this.FBKName = FBKName;
    }

    public String getFBKAddress() {
        return FBKAddress;
    }

    public void setFBKAddress(String FBKAddress) {
        this.FBKAddress = FBKAddress;
    }

    public String getFBKEmail() {
        return FBKEmail;
    }

    public void setFBKEmail(String FBKEmail) {
        this.FBKEmail = FBKEmail;
    }
}
