package com.tspolice.echallan.models.restful;

import java.io.Serializable;

public class LoginRespModel implements Serializable {

    private String respCode;

    private LoginRespRemarkModel respRemark;

    private String respDesc;

    public LoginRespModel() {
        // default constructor
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public LoginRespRemarkModel getRespRemark() {
        return respRemark;
    }

    public void setRespRemark(LoginRespRemarkModel respRemark) {
        this.respRemark = respRemark;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    @Override
    public String toString() {
        return "ClassPojo [respCode = " + respCode + ", respRemark = " + respRemark + ", respDesc = " + respDesc + "]";
    }
}
