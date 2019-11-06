package com.tspolice.echallan.models.restful;

import java.io.Serializable;

public class PsMasterModel implements Serializable {
    private int psCode;
    private String zoneCode, tti_addr, ps_name, ps_sf, contact_no,
            email_id, dept_cd, cmpdbooth_Addr, cmpdbooth_contact_no;

    public PsMasterModel() {
        // default constructor
    }

    public int getPsCode() {
        return psCode;
    }

    public void setPsCode(int psCode) {
        this.psCode = psCode;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getTtiAddress() {
        return tti_addr;
    }

    public void setTtiAddress(String ttiAddress) {
        this.tti_addr = ttiAddress;
    }

    public String getPsName() {
        return ps_name;
    }

    public void setPsName(String psName) {
        this.ps_name = psName;
    }

    public String getPsSF() {
        return ps_sf;
    }

    public void setPsSF(String psSF) {
        this.ps_sf = psSF;
    }

    public String getContactNo() {
        return contact_no;
    }

    public void setContactNo(String contactNo) {
        this.contact_no = contactNo;
    }

    public String getEmailId() {
        return email_id;
    }

    public void setEmailId(String emailId) {
        this.email_id = emailId;
    }

    public String getDeptCode() {
        return dept_cd;
    }

    public void setDeptCode(String deptCode) {
        this.dept_cd = deptCode;
    }

    public String getCompoundingBoothAddress() {
        return cmpdbooth_Addr;
    }

    public void setCompoundingBoothAddress(String compoundingBoothAddress) {
        this.cmpdbooth_Addr = compoundingBoothAddress;
    }

    public String getCompoundingBoothNo() {
        return cmpdbooth_contact_no;
    }

    public void setCompoundingBoothNo(String compoundingBoothNo) {
        this.cmpdbooth_contact_no = compoundingBoothNo;
    }
}
