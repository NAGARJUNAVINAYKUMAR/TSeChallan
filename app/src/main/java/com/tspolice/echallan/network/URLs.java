package com.tspolice.echallan.network;

public class URLs {

    private static final String rootUrl = "http://61.95.168.181:8888/echallan/";

    private static final String officerDetails = rootUrl + "officerDetails";

    public static String officerDetails(String pid, String password) {
        return officerDetails + "?" + URLParams.userID + "=" + pid
                + "&" + URLParams.pwd + "=" + password;
    }

    private static final String unitMaster = rootUrl + "unitMaster";

    public String unitMaster(String stateCode) {
        return unitMaster + "?" + URLParams.stateCode + "=" + stateCode;
    }

    private static final String psMasterbyUnit = rootUrl + "psMasterbyUnit";

    public String psMasterbyUnit(String unitCode) {
        return psMasterbyUnit + "?" + URLParams.unitCode + "=" + unitCode;
    }

    public static final String cadreMaster = rootUrl + "cadreMaster";
    public static final String userRegistration = rootUrl + "userRegistration";
    private static final String sendOTP = rootUrl + "sendOTP";

    public String sendOTP(String mobileNo) {
        return sendOTP + "?" + URLParams.mobileNo + "=" + mobileNo;
    }

    private static final String pointsMaster = rootUrl + "pointsMaster";

    public String pointsMaster(String psCode) {
        return pointsMaster + "?" + URLParams.psCode + "=" + psCode;
    }

    public static final String wheelerMaster = rootUrl + "wheelerMaster";

    private static final String getRTADetails = rootUrl + "rta/rcdl";

    public static String getRTADetails(String regnNo, String dlNo, String dlDOB, String challanBaseCd) {
        return getRTADetails + "?" + URLParams.regnNo + "=" + regnNo
                + "&" + URLParams.dlNo + "=" + dlNo
                + "&" + URLParams.dlDOB + "=" + dlDOB
                + "&" + URLParams.challanBaseCd + "=" + challanBaseCd;
    }

    private static final String sectionMasterByWheeler = rootUrl + "sectionMasterByWheeler";

    public String sectionMasterByWheeler(String wheelerCd) {
        return sectionMasterByWheeler + "?" + URLParams.wheelerCd + "=" + wheelerCd;
    }

    private static final String wheelerMasterByVClassId = rootUrl + "wheelerMasterByVClassId";

    public static String wheelerMasterByVClassId(String vClassId) {
        return wheelerMasterByVClassId + "?" + URLParams.VClassId + "=" + vClassId;
    }

    public static final String spotChallanGen = rootUrl + "spotChallanGen";

    // soap variables
    public static final String namespace = "http://service.et.mtpv.com";
    private static final String live = "https://echallan.tspolice.gov.in/TSeTicketMobile";
    private static final String test = "http://125.16.1.70:8080/TSeTicketMobile";
    private static final String fix = "/services/MobileEticketServiceImpl?wsdl";
    static final String url = live + fix;
    private static final String authenticateUser = "authenticateUser";
    static final String soapAction = namespace + authenticateUser;
    public static final String authenticateDeviceNPID = "authenticateDeviceNPID";
    public static final String getPsNames = "getPsNames";
    public static final String getPointNamesByPsName = "getPointNamesByPsName";
    public static final String getRTADetailsByVehicleNO = "getRTADetailsByVehicleNO";
    public static final String getRTADetailsByLicenceNo = "getRTADetailsByLicenceNo";
    public static final String getAADHARData = "getAADHARData";
    public static final String getPendingChallansByRegnno = "getPendingChallansByRegnno";
    public static final String getOffenderRemarks = "getOffenderRemarks";
    public static final String getOffenceDetailsbyWheelerChallanTypeUnitRemark = "getOffenceDetailsbyWheelerChallanTypeUnitRemark";
    public static final String getDetainedItems = "getDetainedItems";
}
