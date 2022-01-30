package com.programmergwin.valwithorphans;

public class PaymentSummaryModel {
    public String EducationPercentage;
    public String FoodPercentage;
    public String ShelterPercentage;
    public String FundPercentage;
    public String TotalDonationAmount;
    public String TotalDonationTarget;

    public PaymentSummaryModel(String educationPercentage, String foodPercentage, String shelterPercentage, String fundPercentage, String totalDonationAmount, String totalDonationTarget) {
        EducationPercentage = educationPercentage;
        FoodPercentage = foodPercentage;
        ShelterPercentage = shelterPercentage;
        FundPercentage = fundPercentage;
        TotalDonationAmount = totalDonationAmount;
        TotalDonationTarget = totalDonationTarget;
    }
}
