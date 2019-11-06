package com.tspolice.echallan.models.restful;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RCEntityModel implements Serializable {

    @SerializedName("regnNo")
    private String regnNo;

    @SerializedName("o_name")
    private String ownerName;

    @SerializedName("address")
    private String address;

    @SerializedName("veh_type")
    private String vehicleType;

    @SerializedName("chas_no")
    private String chassisNo;

    @SerializedName("eng_no")
    private String engineNo;

    @SerializedName("insurance_validity")
    private String insuranceValidity;

    @SerializedName("vclass_id")
    private String vClassId;

    public RCEntityModel() {
        // default constructor
    }

    public String getRegnNo() {
        return regnNo;
    }

    public void setRegnNo(String regnNo) {
        this.regnNo = regnNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getInsuranceValidity() {
        return insuranceValidity;
    }

    public void setInsuranceValidity(String insuranceValidity) {
        this.insuranceValidity = insuranceValidity;
    }

    public String getVClassId() {
        return vClassId;
    }

    public void setVClassId(String vClassId) {
        this.vClassId = vClassId;
    }
}
