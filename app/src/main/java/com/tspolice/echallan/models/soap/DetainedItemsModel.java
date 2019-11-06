package com.tspolice.echallan.models.soap;

public class DetainedItemsModel {

    private String wheelerCode, detainedItem, challanNo, licenceNo, aadhaarNo, contactNo, driverName, minorFlagCode;

    public DetainedItemsModel() {
        // empty constructor
    }

    public String getWheelerCode() {
        return wheelerCode;
    }

    public void setWheelerCode(String wheelerCode) {
        this.wheelerCode = wheelerCode;
    }

    public String getDetainedItem() {
        return detainedItem;
    }

    public void setDetainedItem(String detainedItem) {
        this.detainedItem = detainedItem;
    }

    public String getChallanNo() {
        return challanNo;
    }

    public void setChallanNo(String challanNo) {
        this.challanNo = challanNo;
    }

    public String getLicenceNo() {
        return licenceNo;
    }

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getMinorFlagCode() {
        return minorFlagCode;
    }

    public void setMinorFlagCode(String minorFlagCode) {
        this.minorFlagCode = minorFlagCode;
    }
}
