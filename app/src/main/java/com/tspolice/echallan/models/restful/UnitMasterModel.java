package com.tspolice.echallan.models.restful;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UnitMasterModel implements Serializable {

    @SerializedName("stateCode")
    private int stateCode;

    @SerializedName("unit_name")
    private String unitName;

    @SerializedName("unit_sf")
    private String unitSF;

    @SerializedName("helpLine_No")
    private int helpLineNo;

    @SerializedName("unit_cd")
    private int unitCode;

    @SerializedName("tti_Addr")
    private String ttiAddress;

    @SerializedName("cmdbooth_Addr")
    private String compoundBoothAddress;

    @SerializedName("cmdbooth_No")
    private int compoundBoothNo;

    public UnitMasterModel() {
        // default constructor
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitSF() {
        return unitSF;
    }

    public void setUnitSF(String unitSF) {
        this.unitSF = unitSF;
    }

    public int getHelpLineNo() {
        return helpLineNo;
    }

    public void setHelpLineNo(int helpLineNo) {
        this.helpLineNo = helpLineNo;
    }

    public int getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(int unitCode) {
        this.unitCode = unitCode;
    }

    public String getTtiAddress() {
        return ttiAddress;
    }

    public void setTtiAddress(String ttiAddress) {
        this.ttiAddress = ttiAddress;
    }

    public String getCompoundBoothAddress() {
        return compoundBoothAddress;
    }

    public void setCompoundBoothAddress(String compoundBoothAddress) {
        this.compoundBoothAddress = compoundBoothAddress;
    }

    public int getCompoundBoothNo() {
        return compoundBoothNo;
    }

    public void setCompoundBoothNo(int compoundBoothNo) {
        this.compoundBoothNo = compoundBoothNo;
    }
}
