package com.tspolice.echallan.models.soap;

import java.io.Serializable;

public class PendingChallansModel implements Serializable {

    private int id;
    private Boolean isSelected;
    private String vehicleNo, challanNo, offenceDate, offenceTime, placeOfViolation, trafficPSLimits,
            offenceDescription, totalAmount, unknown1, fineAmount, userCharges, offenceUnitCode;

    public PendingChallansModel() {
        // empty constructor
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getChallanNo() {
        return challanNo;
    }

    public void setChallanNo(String challanNo) {
        this.challanNo = challanNo;
    }

    public String getOffenceDate() {
        return offenceDate;
    }

    public void setOffenceDate(String offenceDate) {
        this.offenceDate = offenceDate;
    }

    public String getOffenceTime() {
        return offenceTime;
    }

    public void setOffenceTime(String offenceTime) {
        this.offenceTime = offenceTime;
    }

    public String getPlaceOfViolation() {
        return placeOfViolation;
    }

    public void setPlaceOfViolation(String placeOfViolation) {
        this.placeOfViolation = placeOfViolation;
    }

    public String getTrafficPSLimits() {
        return trafficPSLimits;
    }

    public void setTrafficPSLimits(String trafficPSLimits) {
        this.trafficPSLimits = trafficPSLimits;
    }

    public String getOffenceDescription() {
        return offenceDescription;
    }

    public void setOffenceDescription(String offenceDescription) {
        this.offenceDescription = offenceDescription;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUnknown1() {
        return unknown1;
    }

    public void setUnknown1(String unknown1) {
        this.unknown1 = unknown1;
    }

    public String getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(String fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getUserCharges() {
        return userCharges;
    }

    public void setUserCharges(String userCharges) {
        this.userCharges = userCharges;
    }

    public String getOffenceUnitCode() {
        return offenceUnitCode;
    }

    public void setOffenceUnitCode(String offenceUnitCode) {
        this.offenceUnitCode = offenceUnitCode;
    }
}
