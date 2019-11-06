package com.tspolice.echallan.models.restful;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DLEntityModel implements Serializable {

    @SerializedName("ldcDlNO")
    private String dlNo;

    @SerializedName("ldc_L_NM")
    private String ldc_L_NM;

    @SerializedName("ldc_F_NM")
    private String dlFatherName;

    @SerializedName("ldc_PG_NM")
    private String dlName;

    @SerializedName("ldc_PADDR2")
    private String dlAddress;

    @SerializedName("traffic_POINTS")
    private Integer dlPenaltyPoints;

    @SerializedName("ldc_SEX")
    private String dlGender;

    public DLEntityModel() {
        // default constructor
    }

    public String getDlNo() {
        return dlNo;
    }

    public void setDlNo(String dlNo) {
        this.dlNo = dlNo;
    }

    public String getLdc_L_NM() {
        return ldc_L_NM;
    }

    public void setLdc_L_NM(String ldc_L_NM) {
        this.ldc_L_NM = ldc_L_NM;
    }

    public String getDlFatherName() {
        return dlFatherName;
    }

    public void setDlFatherName(String dlFatherName) {
        this.dlFatherName = dlFatherName;
    }

    public String getDlName() {
        return dlName;
    }

    public void setDlName(String dlName) {
        this.dlName = dlName;
    }

    public String getDlAddress() {
        return dlAddress;
    }

    public void setDlAddress(String dlAddress) {
        this.dlAddress = dlAddress;
    }

    public Integer getDlPenaltyPoints() {
        return dlPenaltyPoints;
    }

    public void setDlPenaltyPoints(Integer dlPenaltyPoints) {
        this.dlPenaltyPoints = dlPenaltyPoints;
    }

    public String getDlGender() {
        return dlGender;
    }

    public void setDlGender(String dlGender) {
        this.dlGender = dlGender;
    }
}
