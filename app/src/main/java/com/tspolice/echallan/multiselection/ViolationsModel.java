package com.tspolice.echallan.multiselection;

public class ViolationsModel {

    private int id;
    private Boolean isSelected;
    private String offenceCode, offenceSection, offenceDesc, totalViolation, minAmount, maxAmount, avgAmount, wheelerCode, detainedStatus, chargeSheetStatus;

    public ViolationsModel() {
        // empty constructor
    }

    public ViolationsModel(int id, String totalViolation) {
        this.id = id;
        this.totalViolation = totalViolation;
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

    public String getOffenceCode() {
        return offenceCode;
    }

    public void setOffenceCode(String offenceCode) {
        this.offenceCode = offenceCode;
    }

    public String getOffenceSection() {
        return offenceSection;
    }

    public void setOffenceSection(String offenceSection) {
        this.offenceSection = offenceSection;
    }

    public String getOffenceDesc() {
        return offenceDesc;
    }

    public void setOffenceDesc(String offenceDesc) {
        this.offenceDesc = offenceDesc;
    }

    public String getTotalViolation() {
        return totalViolation;
    }

    public void setTotalViolation(String totalViolation) {
        this.totalViolation = totalViolation;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getAvgAmount() {
        return avgAmount;
    }

    public void setAvgAmount(String avgAmount) {
        this.avgAmount = avgAmount;
    }

    public String getWheelerCode() {
        return wheelerCode;
    }

    public void setWheelerCode(String wheelerCode) {
        this.wheelerCode = wheelerCode;
    }

    public String getDetainedStatus() {
        return detainedStatus;
    }

    public void setDetainedStatus(String detainedStatus) {
        this.detainedStatus = detainedStatus;
    }

    public String getChargeSheetStatus() {
        return chargeSheetStatus;
    }

    public void setChargeSheetStatus(String chargeSheetStatus) {
        this.chargeSheetStatus = chargeSheetStatus;
    }
}
