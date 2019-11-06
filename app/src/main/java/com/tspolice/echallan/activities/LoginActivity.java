package com.tspolice.echallan.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.tspolice.echallan.R;
import com.tspolice.echallan.models.soap.LoginModel;
import com.tspolice.echallan.network.SoapResponseCallbackListener;
import com.tspolice.echallan.network.PidSecEncrypt;
import com.tspolice.echallan.network.SoapController;
import com.tspolice.echallan.network.URLParams;
import com.tspolice.echallan.network.URLs;
import com.tspolice.echallan.utils.GPSTracker;
import com.tspolice.echallan.utils.Networking;
import com.tspolice.echallan.utils.Constants;
import com.tspolice.echallan.utils.PermissionUtil;
import com.tspolice.echallan.utils.SharedPrefManager;
import com.tspolice.echallan.utils.UiHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener,
        SoapResponseCallbackListener {

    private AppCompatTextView tv_app_name, tv_app_version, tv_forgot_password, tv_register;
    private TextInputEditText et_pid, et_password;
    private AppCompatImageView img_next_login;
    private UiHelper mUiHelper;
    private GPSTracker mGpsTracker;
    private SharedPrefManager mSharedPrefManager;
    private final int splashDialogId = 1, delayMillis = 2000;
    private boolean doubleBackToExitPressedOnce = false;
    private String userPid, password, imeiNo, simSerialNo;
    private double mLatitude = 0.0, mLongitude = 0.0;
    private SoapResponseCallbackListener listener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        setTypefaces();

        initObjects();

        setListeners();

        showDialog(splashDialogId);
        if (!Networking.isNetworkAvailable(this)) {
            mUiHelper.showToastLong(getResources().getString(R.string.network_error));
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    removeDialog(splashDialogId);
                }
            }, delayMillis);
        }

        if ((PermissionUtil.checkPermission(this, PermissionUtil.INT_READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                && (PermissionUtil.checkPermission(this, PermissionUtil.INT_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (PermissionUtil.checkPermission(this, PermissionUtil.INT_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (PermissionUtil.checkPermission(this, PermissionUtil.INT_CAMERA) != PackageManager.PERMISSION_GRANTED)
                && (PermissionUtil.checkPermission(this, PermissionUtil.INT_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            PermissionUtil.requestAllPermissions(this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtil.REQUEST_GROUP_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                StringBuilder result = new StringBuilder();
                int i = 0;
                for (String permission : permissions) {
                    String status;
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        status = getResources().getString(R.string.granted);
                    } else {
                        status = getResources().getString(R.string.denied);
                    }
                    result.append("\n").append(permission).append(":").append(status);
                    i++;
                }
                mUiHelper.showToastLong(getResources().getString(R.string.permissions_granted));
                getDeviceDetails();
            } else {
                PermissionUtil.redirectAppSettings(this);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void initViews() {
        tv_app_name = findViewById(R.id.tv_app_name);
        tv_app_version = findViewById(R.id.tv_app_version);
        et_pid = findViewById(R.id.et_pid);
        et_password = findViewById(R.id.et_password);
        img_next_login = findViewById(R.id.img_next_login);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        tv_register = findViewById(R.id.tv_register);

        et_pid.setText(getResources().getString(R.string.my_pid));
        et_password.setText(getResources().getString(R.string.my_password));
    }

    private void setTypefaces() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/cambriab.ttf");
        tv_app_name.setTypeface(typeface);
        tv_app_version.setTypeface(typeface);
        tv_forgot_password.setTypeface(typeface);
        tv_register.setTypeface(typeface);
    }

    private void initObjects() {
        mUiHelper = new UiHelper(LoginActivity.this);
        mGpsTracker = new GPSTracker(LoginActivity.this);
        mSharedPrefManager = SharedPrefManager.getInstance(this);
    }

    private void setListeners() {
        img_next_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == splashDialogId) {
            final Dialog splash = new Dialog(this, R.style.MultiSelectDialogTheme);
            splash.setCancelable(false);
            splash.setContentView(R.layout.layout_splash);
            RelativeLayout rel_splash = splash.findViewById(R.id.rel_splash);
            rel_splash.setOnClickListener(this);
            return splash;
        } else {
            return super.onCreateDialog(id);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_splash:
                if (!Networking.isNetworkAvailable(this)) {
                    mUiHelper.showToastLong(getResources().getString(R.string.network_error));
                } else {
                    removeDialog(splashDialogId);
                }
                break;
            case R.id.img_next_login:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if ((PermissionUtil.checkPermission(LoginActivity.this,
                            PermissionUtil.INT_READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
                            && (PermissionUtil.checkPermission(LoginActivity.this,
                            PermissionUtil.INT_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                            && (PermissionUtil.checkPermission(LoginActivity.this,
                            PermissionUtil.INT_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                        getDeviceDetails();
                        login();
                    } else {
                        PermissionUtil.redirectAppSettings(this);
                    }
                } else {
                    PermissionUtil.redirectAppSettings(this);
                }
                break;
            case R.id.tv_register:
                mUiHelper.intent(RegisterActivity.class);
                break;
            case R.id.tv_forgot_password:
                forgotPasswordDialog();
                break;
            default:
                break;
        }
    }

    @SuppressLint("HardwareIds")
    private void getDeviceDetails() {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            imeiNo = telephonyManager.getDeviceId();
            if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
                simSerialNo = telephonyManager.getSimSerialNumber();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            imeiNo = "";
            simSerialNo = "";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void login() {
        userPid = Objects.requireNonNull(et_pid.getText()).toString().trim();
        password = Objects.requireNonNull(et_password.getText()).toString().trim();
        if (userPid.isEmpty() && password.isEmpty()) {
            mUiHelper.showToastShortCentre(getString(R.string.pid_required));
            et_pid.requestFocus();
        } else if (userPid.isEmpty()) {
            mUiHelper.showToastShortCentre(getString(R.string.pid_required));
            et_pid.requestFocus();
        } else if (password.isEmpty()) {
            mUiHelper.showToastShortCentre(getString(R.string.password_required));
            et_password.requestFocus();
        } else if (password.length() < 4) {
            mUiHelper.showToastShortCentre(getString(R.string.password_length_should_be_4_digits));
            et_password.requestFocus();
        } else {
            if (!Networking.isNetworkAvailable(this)) {
                mUiHelper.showToastLong(getResources().getString(R.string.network_error));
            } else {
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
                SoapObject requestLogin = new SoapObject("" + URLs.namespace, "" + URLs.authenticateDeviceNPID);
                String toEncrypt = PidSecEncrypt.encryptmd5("" + password);
                requestLogin.addProperty(URLParams.pidCd, "" + userPid);
                requestLogin.addProperty(URLParams.password, "" + toEncrypt);
                requestLogin.addProperty(URLParams.imei, "" + imeiNo);
                requestLogin.addProperty(URLParams.simNo, "" + simSerialNo);
                if (mGpsTracker.canGetLocation()) {
                    mLatitude = mGpsTracker.getLatitude();
                    mLongitude = mGpsTracker.getLongitude();
                }
                requestLogin.addProperty(URLParams.gpsLattitude, "" + mLatitude);
                requestLogin.addProperty(URLParams.gpsLongitude, "" + mLongitude);
                requestLogin.addProperty(URLParams.appVersion, "" + Constants.versionCode);
                SoapController asyncTask = new SoapController(LoginActivity.this, requestLogin, listener);
                asyncTask.execute();
            }
        }
    }

    @Override
    public void onSoapResponse(String response, String flag) {
        if (response == null || "".equals(response)
                || response.isEmpty() || "0".equals(response)
                || "NA".equals(response)) {
            mUiHelper.showToastShort(getResources().getString(R.string.network_error));
        } else {
            switch (response) {
                case "SOAP_FAULT":
                    mUiHelper.showToastShort(getString(R.string.error));
                    break;
                case "1":
                    mUiHelper.showToastShort(getString(R.string.invalid_login_id));
                    break;
                case "2":
                    mUiHelper.showToastShort(getString(R.string.invalid_password));
                    break;
                case "3":
                    mUiHelper.showToastShort(getString(R.string.unautherized_device));
                    break;
                case "4":
                    mUiHelper.showToastShort(getString(R.string.please_contact_echallan));
                    break;
                case "5":
                    mUiHelper.showToastShort(getString(R.string.wrong_password_attempts_exceeded));
                    break;
                default:
                    String[] loginDetails = response.split("\\|");
                    LoginModel loginModel = new LoginModel();
                    loginModel.setPidCode(userPid);
                    loginModel.setUnitCode(userPid.substring(0, 2));
                    loginModel.setPassword(password);
                    loginModel.setPidName(loginDetails[1]);
                    loginModel.setPsCode(loginDetails[2]);
                    loginModel.setPsName(loginDetails[3]);
                    loginModel.setCadreCode(loginDetails[4]);
                    loginModel.setCadreName(loginDetails[5]);
                    loginModel.setContactNo(loginDetails[6]);
                    loginModel.setCurrentAppVersion(loginDetails[7]);
                    loginModel.setRtaFlag(loginDetails[8]);
                    loginModel.setDlFlag(loginDetails[9]);
                    loginModel.setAadhaarFlag(loginDetails[10]);
                    loginModel.setOtpFlag(loginDetails[11]);
                    loginModel.setCashlessFlag(loginDetails[12]);
                    loginModel.setMobileNoFlag(loginDetails[13]);
                    loginModel.setOtp(loginDetails[14]);
                    if (loginDetails.length == 16) {
                        loginModel.setOfficerLoginOtp(loginDetails[15]);
                    }
                    loginModel.setImeiNo(imeiNo);
                    loginModel.setSimSerialNo(simSerialNo);
                    mSharedPrefManager.saveOfficerDetails(SharedPrefManager.OFFICER_DETAILS_PREFS, loginModel);
                    startActivity(new Intent(this, HomeActivity.class));
                    break;
            }
        }
    }

    /*private void officerDetails(String pid, String password) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), false);
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET,
                URLs.officerDetails(pid, password), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mUiHelper.dismissProgressDialog();
                        try {
                            if (response.getInt("respCode") == 1) {
                                //JsonElement jsonElement = new JsonParser().parse(response.getString("respRemark"));
                                //LoginRespRemarkModel loginRespRemarkModel = new Gson().fromJson(new JsonParser().parse(response.getString("respRemark")), LoginRespRemarkModel.class);
                                LoginRespModel loginRespModel = new LoginRespModel();
                                loginRespModel.setRespRemark(new Gson().fromJson(new JsonParser().parse(response.getString("respRemark")), LoginRespRemarkModel.class));
                                mSharedPrefManager.saveOfficerDetails(SharedPrefManager.OFFICER_DETAILS_PREFS, loginRespModel);
                                mUiHelper.intent(HomeActivity.class);
                            } else {
                                mUiHelper.showToastShortCentre(getResources().getString(R.string.try_again));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mUiHelper.showToastShortCentre(getResources().getString(R.string.something_went_wrong));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mUiHelper.dismissProgressDialog();
                mUiHelper.showToastShortCentre(getResources().getString(R.string.error));
            }
        }));
    }*/

    private void forgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_forgot_password, null);
        builder.setView(view);
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        mUiHelper.showToastShort(getResources().getString(R.string.click_back_again_to_close));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, delayMillis);
    }
}
