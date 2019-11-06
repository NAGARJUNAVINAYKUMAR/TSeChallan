package com.tspolice.echallan.models.restful;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginRespRemarkModel implements Serializable {

    /*@SerializedName("contact_no")
    private String contactNo;*/

    @SerializedName("state_cd")
    private int stateCode;

    @SerializedName("zone_CD")
    private int zoneCode;

    @SerializedName("cadre_cd")
    private int cadreCode;

    @SerializedName("contact_no")
    private String contactNo;

    @SerializedName("division_CD")
    private int divisionCode;

    @SerializedName("zone_NAME")
    private String zoneName;

    @SerializedName("ps_name")
    private String psName;

    @SerializedName("empId")
    private String empId;

    @SerializedName("state_name")
    private String stateName;

    @SerializedName("ps_cd")
    private int psCode;

    @SerializedName("userID")
    private String userId;

    @SerializedName("pwd")
    private String pwd;

    @SerializedName("emp_name")
    private String empName;

    @SerializedName("actvie_status")
    private int activeStatus;

    @SerializedName("division_NAME")
    private String divisionName;

    @SerializedName("duties")
    private String duties;

    @SerializedName("unitcd")
    private int unitCode;

    @SerializedName("role_cd")
    private int roleCode;

    @SerializedName("unit_name")
    private String unitName;

    @SerializedName("email_id")
    private String emailId;

    @SerializedName("cadre_sf")
    private String cadreSF;

    @SerializedName("unit_sf")
    private String unitSF;

    @SerializedName("dept_cd")
    private int deptCode;

    public LoginRespRemarkModel() {
        // default constructor
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public int getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(int zoneCode) {
        this.zoneCode = zoneCode;
    }

    public int getCadreCode() {
        return cadreCode;
    }

    public void setCadreCode(int cadreCode) {
        this.cadreCode = cadreCode;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public int getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(int divisionCode) {
        this.divisionCode = divisionCode;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getPsName() {
        return psName;
    }

    public void setPsName(String psName) {
        this.psName = psName;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public int getPsCode() {
        return psCode;
    }

    public void setPsCode(int psCode) {
        this.psCode = psCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public int getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(int activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public int getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(int unitCode) {
        this.unitCode = unitCode;
    }

    public int getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(int roleCode) {
        this.roleCode = roleCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getCadreSF() {
        return cadreSF;
    }

    public void setCadreSF(String cadreSF) {
        this.cadreSF = cadreSF;
    }

    public String getUnitSF() {
        return unitSF;
    }

    public void setUnitSF(String unitSF) {
        this.unitSF = unitSF;
    }

    public int getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(int deptCode) {
        this.deptCode = deptCode;
    }
}
