package com.tspolice.echallan.models.restful;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WheelerMasterModel implements Serializable {

    //@SerializedName("wheelercd")
    private String wheelerCode;

    //@SerializedName("vclass_id")
    private String vehicleClassId;

    //@SerializedName("veh_class")
    private String vehicleClass;

    public WheelerMasterModel() {
        // default constructor
    }

    public String getWheelerCode() {
        return wheelerCode;
    }

    public void setWheelerCode(String wheelerCode) {
        this.wheelerCode = wheelerCode;
    }

    public String getVehicleClassId() {
        return vehicleClassId;
    }

    public void setVehicleClassId(String vehicleClassId) {
        this.vehicleClassId = vehicleClassId;
    }

    public String getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(String vehicleClass) {
        this.vehicleClass = vehicleClass;
    }
}
