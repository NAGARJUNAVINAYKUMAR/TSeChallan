package com.tspolice.echallan.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.tspolice.echallan.R;
import com.tspolice.echallan.database.DBFunctions;
import com.tspolice.echallan.models.GlobalModel;
import com.tspolice.echallan.models.soap.DetainedItemsModel;
import com.tspolice.echallan.models.soap.LoginModel;
import com.tspolice.echallan.models.soap.PendingChallansModel;
import com.tspolice.echallan.multiselection.ViolationsModel;
import com.tspolice.echallan.network.SoapResponseCallbackListener;
import com.tspolice.echallan.network.SoapController;
import com.tspolice.echallan.network.URLParams;
import com.tspolice.echallan.network.URLs;
import com.tspolice.echallan.utils.CameraUtils;
import com.tspolice.echallan.utils.Constants;
import com.tspolice.echallan.utils.DateUtils;
import com.tspolice.echallan.utils.GPSTracker;
import com.tspolice.echallan.utils.Networking;
import com.tspolice.echallan.utils.PermissionUtil;
import com.tspolice.echallan.utils.SharedPrefManager;
import com.tspolice.echallan.utils.SingleMediaScanner;
import com.tspolice.echallan.utils.UiHelper;
import com.tspolice.echallan.utils.VibratorUtils;
import com.tspolice.echallan.violationselection.ViolationsSelectDialog;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SpotChallanActivity extends AppCompatActivity implements
        View.OnClickListener,
        SoapResponseCallbackListener {

    private static final String TAG = "SpotChallanAct-->";

    AppCompatTextView tv_challan_type_header, tv_vehicle_no_header, tv_star_vehicle_no, tv_star_driver;

    RadioGroup rg_challan_base;

    AppCompatRadioButton rb_regn_no, rb_tr_no, rb_engine_no, rb_chassis_no;

    AppCompatEditText et_regn_no, et_dl_no, et_dl_dob, et_driver_name, et_driver_father_name, et_driver_address, et_driver_age;

    AppCompatButton btn_get_rta_details, spinner_ps_name, spinner_point_name, spinner_resp_ps_name, spinner_wheeler_code,
            spinner_violations, btn_cancel, btn_next;

    LinearLayout lnr_lyt_rc_details, lnr_lyt_dl_details, lnr_lyt_pending_challans, lnr_lyt_driver_information,
            lnr_lyt_bac_serial_no, lnr_lyt_bac_reading, lnr_lyt_selected_violations_card_display,
            lnr_lyt_selected_violations, lnr_lyt_total_amount_display, lnr_lyt_camera_gallery, lnr_lyt_btns;

    AppCompatTextView tv_regd_number, tv_rc_regd_number, tv_rc_owner_name, tv_rc_owner_address, tv_rc_vehicle_type,
            tv_rc_chassis_no, tv_rc_engine_no, tv_rc_insurance_validity, tv_rc_details_not_found;

    AppCompatTextView tv_dl_number, tv_dl_name, tv_dl_father_name, tv_dl_address, tv_dl_age, tv_dl_gender,
            tv_dl_issued_on, tv_dl_penalty_points, tv_dl_details_not_found;

    AppCompatTextView tv_no_of_pending_challans, tv_total_pending_amount, tv_pending_challans_not_found, tv_total_fine_amount_display;

    AppCompatImageView img_dob_select, img_dl_img, img_camera, img_gallery, img_display;

    ListView lv_selected_violations;

    private UiHelper mUiHelper;

    private SharedPrefManager mSharedPrefManager;

    private GPSTracker mGpsTracker;

    private ViolationsSelectDialog violationsSelectDialog;

    private LoginModel loginModel;

    private ArrayList<ViolationsModel> violationsList, selectedViolationsList;

    private ArrayList<Integer> previouslySelectedIdsList = new ArrayList<>();

    private ArrayList<PendingChallansModel> pendingChallansList;

    private ArrayList<DetainedItemsModel> detainedItemsList;

    private String pidCode, unitCode, unitName, psCode, psName, pointCode, pointName, respPsCode, respPsName,
            regnNo, licenceNo, licenceDOB, rcData, driverName, offenceDate, offenceTime, wheelerName,
            driverImageData, offenderRemarks, detainedStatus;

    private String challanBaseCd = "0", wheelerCode = "0", imgFromCameraGallery = "0", dlValidFlag = "V",
            penaltyPoints = "0", theftVehicle = "N", chargeSheetStatus = "0";

    private static String imageStoragePath;

    private HashMap<String, String> psNamesHashMap, pointNamesHashMap, wheelerNamesHashMap;

    private String[] psCodesArray, psNamesArray, pointCodesArray, pointNamesArray, rcDetailsArray, dlDetailsArray,
            wheelerCodesArray, wheelerNamesArray, offenderRemarksArray, offenderRemarksChildArray;

    private int spotOtp = 0, spotOtpTime = 0, year, month, day, hours, minutes, selectedPsCode = -1, selectedPointCode = -1,
            selectedRespPsCode = -1, selectedWheelerCode = -1;

    private double pendingAmount = 0.0, totalFineAmount = 0.0;

    private boolean licenceStatus = false, dobCheck = false, soldOutVehicle = false, chargeSheetState = false, isSelectedWithoutDL = false;

    private static final int REQUEST_SELECT_PENDING_CHALLANS = 110;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CameraUtils.KEY_IMAGE_PATH, imageStoragePath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imageStoragePath = savedInstanceState.getString(CameraUtils.KEY_IMAGE_PATH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_challan);

        initViews();

        initObjects();

        setListeners();

        restoreFromBundle(savedInstanceState);

        getWheelerDetailsFromDataBase();

        if (!"".equals(mSharedPrefManager.getString(Constants.PS_NAME))) {
            spinner_ps_name.setText(mSharedPrefManager.getString(Constants.PS_NAME));
            spinner_ps_name.setClickable(false);
        }
        if (!"".equals(mSharedPrefManager.getString(Constants.POINT_NAME))) {
            spinner_point_name.setText(mSharedPrefManager.getString(Constants.POINT_NAME));
        }
        if (!"".equals(mSharedPrefManager.getString(Constants.RESP_PS_NAME))) {
            spinner_resp_ps_name.setText(mSharedPrefManager.getString(Constants.RESP_PS_NAME));
        }

        if (!Networking.isNetworkAvailable(SpotChallanActivity.this)) {
            mUiHelper.showToastLong(getResources().getString(R.string.network_error));
        } else {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            SoapObject requestPsNames = new SoapObject("" + URLs.namespace, "" + URLs.getPsNames);
            requestPsNames.addProperty(URLParams.unitCd, "" + unitCode);
            SoapController asyncTaskPsNames = new SoapController(this, requestPsNames, this, "" + Constants.PS_NAMES);
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            asyncTaskPsNames.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.spinner_ps_name:
                mUiHelper.showToastShort(getResources().getString(R.string.ps_name_cannot_be_changed));
                break;
            case R.id.spinner_point_name:
                AlertDialog.Builder builderPoints = new AlertDialog.Builder(SpotChallanActivity.this);
                builderPoints.setTitle(getResources().getString(R.string.select_point_name));
                builderPoints.setCancelable(false);
                builderPoints.setSingleChoiceItems(pointNamesArray, selectedPointCode, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedPointCode = which;
                        pointCode = pointCodesArray[which];
                        pointName = pointNamesArray[which];
                        spinner_point_name.setText(pointName);
                        mSharedPrefManager.putString(Constants.POINT_CODE, pointCode);
                        mSharedPrefManager.putString(Constants.POINT_NAME, pointName);
                        dialog.cancel();
                    }
                });
                builderPoints.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialogPoints = builderPoints.create();
                dialogPoints.show();
                break;
            case R.id.spinner_resp_ps_name:
                AlertDialog.Builder builderRespPsNames = new AlertDialog.Builder(SpotChallanActivity.this);
                builderRespPsNames.setTitle(getResources().getString(R.string.select_point_name));
                builderRespPsNames.setCancelable(false);
                builderRespPsNames.setSingleChoiceItems(psNamesArray, selectedRespPsCode, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedRespPsCode = which;
                        respPsCode = psCodesArray[which];
                        respPsName = psNamesArray[which];
                        spinner_resp_ps_name.setText(respPsName);
                        mSharedPrefManager.putString(Constants.RESP_PS_CODE, respPsCode);
                        mSharedPrefManager.putString(Constants.RESP_PS_NAME, respPsName);
                        dialog.cancel();
                    }
                });
                builderRespPsNames.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialogRespPsNames = builderRespPsNames.create();
                dialogRespPsNames.show();
                break;
            case R.id.img_dob_select:
                dobCheck = false;
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(SpotChallanActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                licenceDOB = dayOfMonth + "/" + (month + 1) + "/" + year;
                                try {
                                    licenceDOB = DateUtils.getDateFormat(licenceDOB);
                                    Log.i(TAG, "licenceDOB-->" + licenceDOB);
                                    et_dl_dob.setText(licenceDOB);
                                    dobCheck = true;
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    dobCheck = false;
                                }
                            }
                        }, year, month, day);
                datePickerDialog.show();
                break;
            case R.id.btn_get_rta_details:
                lnr_lyt_rc_details.setVisibility(View.GONE);
                lnr_lyt_dl_details.setVisibility(View.GONE);
                lnr_lyt_pending_challans.setVisibility(View.GONE);
                lnr_lyt_driver_information.setVisibility(View.VISIBLE);
                lnr_lyt_selected_violations_card_display.setVisibility(View.GONE);
                if (lnr_lyt_selected_violations != null) {
                    lnr_lyt_selected_violations.removeAllViews();
                }
                GlobalModel.pendingChallansList = null;
                ViolationsSelectDialog.selectedIdsForCallback = null;

                regnNo = Objects.requireNonNull(et_regn_no.getText()).toString().trim();
                licenceNo = Objects.requireNonNull(et_dl_no.getText()).toString().trim();
                licenceDOB = Objects.requireNonNull(et_dl_dob.getText()).toString().trim();

                if (getResources().getString(R.string.select_ps_name)
                        .equalsIgnoreCase(spinner_ps_name.getText().toString().trim())) {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.select_ps_name));
                } else if (getResources().getString(R.string.select_point_name)
                        .equalsIgnoreCase(spinner_point_name.getText().toString().trim())) {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.select_point_name));
                } else if (getResources().getString(R.string.select_resp_ps_name)
                        .equalsIgnoreCase(spinner_resp_ps_name.getText().toString().trim())) {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.select_resp_ps_name));
                } else if (regnNo.isEmpty()) {
                    et_regn_no.requestFocus();
                    mUiHelper.showToastShortCentre(getString(R.string.please_enter_vehicle_no));
                } else if (!licenceNo.isEmpty() && licenceNo.length() < 6) {
                    et_dl_no.requestFocus();
                    mUiHelper.showToastShortCentre(getString(R.string.please_enter_valid_licence_no));
                } else if (!licenceNo.isEmpty() && licenceDOB.isEmpty()) {
                    mUiHelper.showToastShortCentre(getString(R.string.please_select_dob_of_dl));
                } else {
                    if (licenceNo.isEmpty()) {
                        licenceStatus = true;
                        showAlertDialog("" + getResources().getString(R.string.dl_flag), "" + getResources().getString(R.string.alert), "" + getResources().getString(R.string.please_enter_valid_licence_no_or_add_without_dl_carrying_dl));
                    }
                    if (!Networking.isNetworkAvailable(SpotChallanActivity.this)) {
                        mUiHelper.showToastLong(getResources().getString(R.string.network_error));
                    } else {
                        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
                        SoapObject requestRC = new SoapObject("" + URLs.namespace, "" + URLs.getRTADetailsByVehicleNO);
                        requestRC.addProperty(URLParams.vehicleNo, regnNo);
                        SoapController asyncTaskRC = new SoapController(this, requestRC, this, "" + Constants.RC);
                        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                        asyncTaskRC.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                        if (!licenceNo.isEmpty() && licenceNo.length() > 5) {
                            SoapObject requestDL = new SoapObject("" + URLs.namespace, "" + URLs.getRTADetailsByLicenceNo);
                            requestDL.addProperty(URLParams.licenceNo, licenceNo);
                            requestDL.addProperty(URLParams.dob, licenceDOB);
                            SoapController asyncTaskDL = new SoapController(this, requestDL, this, "" + Constants.DL);
                            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                            asyncTaskDL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                        SoapObject requestPC = new SoapObject("" + URLs.namespace, "" + URLs.getPendingChallansByRegnno);
                        requestPC.addProperty(URLParams.regnNo, regnNo);
                        requestPC.addProperty(URLParams.drvierLicNo, licenceNo);
                        requestPC.addProperty(URLParams.ownerLicNo, licenceNo);
                        requestPC.addProperty(URLParams.pidCd, pidCode);
                        requestPC.addProperty(URLParams.pidName, loginModel.getPidName());
                        requestPC.addProperty(URLParams.password, loginModel.getPassword());
                        requestPC.addProperty(URLParams.simId, loginModel.getSimSerialNo());
                        requestPC.addProperty(URLParams.imeiNo, loginModel.getImeiNo());
                        requestPC.addProperty(URLParams.latitude, String.valueOf(mGpsTracker.getLatitude()));
                        requestPC.addProperty(URLParams.longitude, String.valueOf(mGpsTracker.getLongitude()));
                        requestPC.addProperty(URLParams.ip, "");
                        requestPC.addProperty(URLParams.unitCode, unitCode);
                        SoapController asyncTaskPC = new SoapController(this, requestPC, this, "" + Constants.PC);
                        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
                        asyncTaskPC.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
                break;
            case R.id.spinner_wheeler_code:
                regnNo = Objects.requireNonNull(et_regn_no.getText()).toString().trim();
                if (getResources().getString(R.string.select_ps_name)
                        .equalsIgnoreCase(spinner_ps_name.getText().toString().trim())) {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.select_ps_name));
                } else if (getResources().getString(R.string.select_point_name)
                        .equalsIgnoreCase(spinner_point_name.getText().toString().trim())) {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.select_point_name));
                } else if (getResources().getString(R.string.select_resp_ps_name)
                        .equalsIgnoreCase(spinner_resp_ps_name.getText().toString().trim())) {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.select_resp_ps_name));
                } else if (regnNo.isEmpty()) {
                    et_regn_no.requestFocus();
                    mUiHelper.showToastShortCentre(getString(R.string.please_enter_vehicle_no));
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SpotChallanActivity.this);
                    builder.setTitle(getResources().getString(R.string.select_wheeler_code));
                    builder.setCancelable(false);
                    builder.setSingleChoiceItems(wheelerNamesArray, selectedWheelerCode, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectedWheelerCode = which;
                            spinner_wheeler_code.setText(wheelerNamesArray[which]);
                            wheelerCode = wheelerCodesArray[which];
                            Log.i(TAG, "selectedWheelerCode-->" + wheelerCode);
                            dialog.cancel();
                            getOffenceDetailsByWheelerChallanTypeUnitRemark(wheelerCode);
                        }
                    });
                    builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            case R.id.spinner_violations:
                regnNo = Objects.requireNonNull(et_regn_no.getText()).toString().trim();
                licenceNo = Objects.requireNonNull(et_dl_no.getText()).toString().trim();
                licenceDOB = Objects.requireNonNull(et_dl_dob.getText()).toString().trim();
                if (getResources().getString(R.string.select_ps_name)
                        .equalsIgnoreCase(spinner_ps_name.getText().toString().trim())) {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.select_ps_name));
                } else if (getResources().getString(R.string.select_point_name)
                        .equalsIgnoreCase(spinner_point_name.getText().toString().trim())) {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.select_point_name));
                } else if (getResources().getString(R.string.select_resp_ps_name)
                        .equalsIgnoreCase(spinner_resp_ps_name.getText().toString().trim())) {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.select_resp_ps_name));
                } else if (regnNo.isEmpty()) {
                    et_regn_no.requestFocus();
                    mUiHelper.showToastShortCentre(getString(R.string.please_enter_vehicle_no));
                } else if (getResources().getString(R.string.select_wheeler_code)
                        .equalsIgnoreCase(spinner_wheeler_code.getText().toString().trim()) || "0".equals(wheelerCode)) {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.select_wheeler_type));
                } else {
                    if (violationsList != null && violationsList.size() > 0) {
                        violationsSelectDialog.show(getSupportFragmentManager(), "violationsSelectDialog");
                    }
                }
                break;
            case R.id.lnr_lyt_pending_challans:
                if (pendingChallansList != null && pendingChallansList.size() > 0) {
                    GlobalModel.pendingChallansList = pendingChallansList;
                    Intent intent = new Intent(this, PendingChallansActivity.class);
                    intent.putExtra("pendingChallansList", pendingChallansList);
                    startActivityForResult(intent, REQUEST_SELECT_PENDING_CHALLANS);
                } else {
                    mUiHelper.showToastShort(getResources().getString(R.string.no_pending_challans));
                }
                break;
            case R.id.img_camera:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (PermissionUtil.checkPermission(this, PermissionUtil.INT_CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                            PermissionUtil.showPermissionExplanation(this, PermissionUtil.INT_CAMERA);
                        } else if (!mSharedPrefManager.getBoolean(Constants.CAMERA)) {
                            PermissionUtil.requestPermission(this, PermissionUtil.INT_CAMERA);
                            mSharedPrefManager.putBoolean(Constants.CAMERA, true);
                        } else {
                            PermissionUtil.redirectAppSettings(this);
                        }
                    } else {
                        if (CameraUtils.isDeviceSupportCamera(this)) {
                            mUiHelper.showToastLongCentre(getResources().getString(R.string.device_camera_doesnt_supports));
                        } else {
                            openCamera();
                        }
                    }
                } else {
                    PermissionUtil.redirectAppSettings(this);
                }
                break;
            case R.id.img_gallery:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (PermissionUtil.checkPermission(this, PermissionUtil.INT_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            PermissionUtil.showPermissionExplanation(this, PermissionUtil.INT_STORAGE);
                        } else if (!mSharedPrefManager.getBoolean(Constants.STORAGE)) {
                            PermissionUtil.requestPermission(this, PermissionUtil.INT_STORAGE);
                            mSharedPrefManager.putBoolean(Constants.STORAGE, true);
                        } else {
                            PermissionUtil.redirectAppSettings(this);
                        }
                    } else {
                        openGallery();
                    }
                } else {
                    PermissionUtil.redirectAppSettings(this);
                }
                break;
            case R.id.btn_cancel:
                finish();
                mUiHelper.intent(HomeActivity.class);
                break;
            case R.id.btn_next:
                regnNo = Objects.requireNonNull(et_regn_no.getText()).toString().trim();
                licenceNo = Objects.requireNonNull(et_dl_no.getText()).toString().trim();
                licenceDOB = Objects.requireNonNull(et_dl_dob.getText()).toString().trim();
                driverName = Objects.requireNonNull(et_driver_name.getText().toString().trim());
                if (getResources().getString(R.string.select_ps_name)
                        .equalsIgnoreCase(spinner_ps_name.getText().toString().trim())) {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.select_ps_name));
                } else if (getResources().getString(R.string.select_point_name)
                        .equalsIgnoreCase(spinner_point_name.getText().toString().trim())) {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.select_point_name));
                } else if (getResources().getString(R.string.select_resp_ps_name)
                        .equalsIgnoreCase(spinner_resp_ps_name.getText().toString().trim())) {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.select_resp_ps_name));
                } else if (regnNo.isEmpty()) {
                    et_regn_no.requestFocus();
                    mUiHelper.showToastShortCentre(getString(R.string.please_enter_vehicle_no));
                } else if (!licenceNo.isEmpty() && licenceNo.length() < 6) {
                    et_dl_no.requestFocus();
                    mUiHelper.showToastShortCentre(getString(R.string.please_enter_valid_licence_no));
                } else if (!licenceNo.isEmpty() && licenceDOB.isEmpty()) {
                    mUiHelper.showToastShortCentre(getString(R.string.please_select_dob_of_dl));
                } else if (et_driver_name.isShown() && driverName.isEmpty()) {
                    mUiHelper.showToastShortCentre(getString(R.string.driver_name_is_required));
                } else if (getResources().getString(R.string.select_wheeler_code)
                        .equalsIgnoreCase(spinner_wheeler_code.getText().toString().trim()) || "0".equals(wheelerCode)) {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.select_wheeler_type));
                } else if (getResources().getString(R.string.select_violations)
                        .equalsIgnoreCase(spinner_violations.getText().toString().trim())) {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.please_select_violations));
                } else if ("1".equals(chargeSheetStatus) && "0".equals(imgFromCameraGallery)) {
                    mUiHelper.showToastShort(getResources().getString(R.string.please_take_drivers_photo));
                } else {
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
                    SoapObject requestDetainedItems = new SoapObject("" + URLs.namespace, "" + URLs.getDetainedItems);
                    requestDetainedItems.addProperty(URLParams.regnNo, regnNo);
                    requestDetainedItems.addProperty(URLParams.unitCode, unitCode);
                    SoapController asyncTaskDetainedItems = new SoapController(this, requestDetainedItems, this, "" + Constants.DETAINED_ITEMS);
                    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                    asyncTaskDetainedItems.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    mUiHelper.intent(SpotSecondActivity.class);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSoapResponse(String response, String flag) {
        if (response == null || response.isEmpty() || "0".equals(response)
                || "null".equals(response) || "NA".equals(response)) {
            if (Constants.RC.equals(flag)) {
                lnr_lyt_rc_details.setVisibility(View.GONE);
                tv_rc_details_not_found.setVisibility(View.VISIBLE);
            }
            if (Constants.DL.equals(flag)) {
                lnr_lyt_dl_details.setVisibility(View.GONE);
                tv_dl_details_not_found.setVisibility(View.VISIBLE);
                lnr_lyt_driver_information.setVisibility(View.VISIBLE);
            }
            if (Constants.PC.equals(flag)) {
                lnr_lyt_pending_challans.setVisibility(View.GONE);
                tv_pending_challans_not_found.setVisibility(View.VISIBLE);
            }
        } else {
            if (Constants.PS_NAMES.equals(flag)) {
                response = response.substring(1);
                String[] split = response.split("!");
                int length = split.length;
                String[][] splitChild = new String[length][3];
                for (int i = 0; i < length; i++) {
                    splitChild[i] = split[i].split("@");
                }
                psNamesHashMap = new HashMap<>();
                psCodesArray = new String[length];
                psNamesArray = new String[length];
                for (int j = 0; j < splitChild.length; j++) {
                    String code = splitChild[j][0];
                    String name = splitChild[j][1];
                    Log.i(Constants.APP_LOG, TAG + code + "&amp;" + name);
                    psCodesArray[j] = code;
                    psNamesArray[j] = name;
                    psNamesHashMap.put(code, name);
                }
                for (String code : psNamesHashMap.keySet()) {
                    if (code.trim().equals(psCode)) {
                        psName = psNamesHashMap.get(code);
                        spinner_ps_name.setText(psName);
                        mSharedPrefManager.putString(Constants.PS_CODE, psCode);
                        mSharedPrefManager.putString(Constants.PS_NAME, psName);
                        spinner_ps_name.setClickable(false);
                        break;
                    }
                }
                getPointNamesByPsCode(psCode);
            }
            if (Constants.POINT_NAMES.equals(flag)) {
                response = response.substring(1);
                String[] split = response.split("!");
                int length = split.length;
                String[][] splitChild = new String[length][3];
                for (int i = 0; i < length; i++) {
                    splitChild[i] = split[i].split("@");
                }
                pointNamesHashMap = new HashMap<>();
                pointCodesArray = new String[length];
                pointNamesArray = new String[length];
                for (int j = 0; j < splitChild.length; j++) {
                    String code = splitChild[j][0];
                    String name = splitChild[j][1];
                    Log.i(Constants.APP_LOG, TAG + code + "&amp;" + name);
                    pointCodesArray[j] = code;
                    pointNamesArray[j] = name;
                    pointNamesHashMap.put(code, name);
                }
            }
            if (Constants.RC.equals(flag)) {
                rcData = response;
                rcDetailsArray = response.split("!");

                lnr_lyt_rc_details.setVisibility(View.VISIBLE);
                tv_rc_details_not_found.setVisibility(View.GONE);

                tv_rc_regd_number.setText(regnNo);
                tv_rc_owner_name.setText(rcDetailsArray[1] != null ? rcDetailsArray[1] : "NA");
                tv_rc_owner_address.setText(rcDetailsArray[2] != null ? rcDetailsArray[2] : "NA"
                        + (rcDetailsArray[3] != null ? rcDetailsArray[3] : ""));
                tv_rc_vehicle_type.setText(rcDetailsArray[4] != null ? rcDetailsArray[4] : "NA"
                        + (rcDetailsArray[5] != null ? rcDetailsArray[5] : "NA")
                        + (rcDetailsArray[6] != null ? rcDetailsArray[6] : "NA"));
                tv_rc_engine_no.setText(rcDetailsArray[7] != null ? rcDetailsArray[7] : "NA");
                tv_rc_chassis_no.setText(rcDetailsArray[8] != null ? rcDetailsArray[8] : "NA");
                tv_rc_insurance_validity.setText(getResources().getString(R.string.dd_mmm_yyyy));

                wheelerCode = rcDetailsArray[9].trim();
                if ("".equals(wheelerCode) || "null".equals(wheelerCode) || "0".equals(wheelerCode) || "NA".equals(wheelerCode)) {
                    spinner_wheeler_code.setEnabled(true);
                    spinner_wheeler_code.setClickable(true);
                } else {
                    for (String code : wheelerNamesHashMap.keySet()) {
                        if (code.trim().equals(wheelerCode)) {
                            wheelerName = wheelerNamesHashMap.get(code);
                            spinner_wheeler_code.setText(wheelerName);
                            spinner_wheeler_code.setClickable(false);
                            break;
                        }
                    }
                    getOffenceDetailsByWheelerChallanTypeUnitRemark(wheelerCode);
                }
                SoapObject requestRemarks = new SoapObject(URLs.namespace, URLs.getOffenderRemarks);
                requestRemarks.addProperty(URLParams.vehicleNo, regnNo + "^" + rcData);
                requestRemarks.addProperty(URLParams.licenceNo, licenceNo);
                requestRemarks.addProperty(URLParams.aadharNo, "");
                SoapController asyncTaskRemarks = new SoapController(this, requestRemarks, this, "" + Constants.OFFENDER_REMARKS);
                Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
                asyncTaskRemarks.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            if (Constants.DL.equals(flag)) {
                lnr_lyt_dl_details.setVisibility(View.VISIBLE);
                tv_dl_details_not_found.setVisibility(View.GONE);
                lnr_lyt_driver_information.setVisibility(View.GONE);

                dlDetailsArray = response.split("!");

                tv_dl_number.setText(licenceNo);
                tv_dl_name.setText(dlDetailsArray[0] != null ? dlDetailsArray[0] : "");
                tv_dl_father_name.setText(dlDetailsArray[1] != null ? dlDetailsArray[1] : "");
                String dlMobileNo = dlDetailsArray[2] != null ? dlDetailsArray[2] : "";
                tv_dl_address.setText(dlDetailsArray[3] != null ? dlDetailsArray[3] : ""
                        + (dlDetailsArray[4] != null ? dlDetailsArray[4] : ""));
                String dlImage = dlDetailsArray[5] != null ? dlDetailsArray[5] : "";
                if ("".equals(dlImage) || "null".equals(dlImage) || "0".equals(dlImage) || "NA".equals(dlImage)) {
                    img_dl_img.setImageResource(R.drawable.ic_profile_img);
                } else {
                    try {
                        byte[] data = Base64.decode(dlDetailsArray[5].trim(), Base64.DEFAULT);
                        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                        img_dl_img.setImageBitmap(bm);
                    } catch (Exception e) {
                        e.printStackTrace();
                        img_dl_img.setImageResource(R.drawable.ic_profile_img);
                    }
                }
                dlValidFlag = dlDetailsArray[6] != null ? dlDetailsArray[6] : "V";
                penaltyPoints = dlDetailsArray[7] != null ? dlDetailsArray[7] : "0";
                tv_dl_penalty_points.setText("TOTAL PENALTY POINTS: " + penaltyPoints);
            }
            if (Constants.PC.equals(flag)) {
                String[] split = response.split("@");
                int length = split.length;
                String[][] splitChild = new String[length][8];
                pendingChallansList = new ArrayList<>(length);
                for (int i = 0; i < length; i++) {
                    splitChild[i] = split[i].split("!");
                    PendingChallansModel model = new PendingChallansModel();
                    model.setId(i);
                    model.setSelected(false);
                    model.setVehicleNo(splitChild[i][0].trim());
                    model.setChallanNo(splitChild[i][1].trim());
                    model.setOffenceDate(splitChild[i][2].trim());
                    model.setOffenceTime(splitChild[i][3].trim());
                    model.setPlaceOfViolation(splitChild[i][4].trim());
                    model.setTrafficPSLimits(splitChild[i][5].trim());
                    model.setOffenceDescription(splitChild[i][6].trim());
                    model.setTotalAmount(splitChild[i][7].trim());
                    model.setUnknown1(splitChild[i][8].trim());
                    model.setFineAmount(splitChild[i][9].trim());
                    model.setUserCharges(splitChild[i][10].trim());
                    model.setOffenceUnitCode(splitChild[i][11].trim());
                    pendingChallansList.add(model);
                }
                int count = splitChild.length;
                if (count > 0) {
                    for (String[] pendingChallansDetail : splitChild) {
                        pendingAmount = pendingAmount + (Double.parseDouble(pendingChallansDetail[7].trim()));
                    }
                    lnr_lyt_pending_challans.setVisibility(View.VISIBLE);
                    tv_pending_challans_not_found.setVisibility(View.GONE);
                    tv_no_of_pending_challans.setText(String.valueOf(count));
                    tv_total_pending_amount.setText(String.valueOf(pendingAmount));
                    Log.i(TAG, "challansCount-->" + count + "&amp;" + pendingAmount);
                } else {
                    lnr_lyt_pending_challans.setVisibility(View.GONE);
                    tv_pending_challans_not_found.setVisibility(View.VISIBLE);
                }
            }
            if (Constants.VIOLATIONS.equals(flag)) {
                Log.i(TAG, "violationsMaster-->" + response);
                response = response.substring(1);
                Log.i(TAG, "violationsMasterSplit-->" + response);
                String[] split = response.split("!");
                int length = split.length;
                String[][] splitChild = new String[length][6];
                violationsList = new ArrayList<>(length);
                for (int i = 0; i < length; i++) {
                    splitChild[i] = split[i].split("@");
                    ViolationsModel violationsModel = new ViolationsModel();
                    violationsModel.setId(i);
                    violationsModel.setOffenceCode(splitChild[i][0].trim());
                    violationsModel.setOffenceSection(splitChild[i][1].trim());
                    violationsModel.setOffenceDesc(splitChild[i][2].trim());
                    violationsModel.setTotalViolation(splitChild[i][2] + " ("
                            + splitChild[i][1] + ")"); // or set offenceCode [" + violationsDetails[i][0] + "]");
                    violationsModel.setMinAmount(splitChild[i][3].trim());
                    violationsModel.setMaxAmount(splitChild[i][4].trim());
                    violationsModel.setAvgAmount(splitChild[i][5].trim());
                    violationsModel.setWheelerCode(splitChild[i][6].trim());
                    violationsModel.setDetainedStatus(splitChild[i][7]);
                    violationsModel.setChargeSheetStatus(splitChild[i][8]);
                    violationsList.add(violationsModel);
                }
                prepareViolationsDialog();
            }
            if (Constants.OFFENDER_REMARKS.equals(flag)) {
                offenderRemarksArray = response.split("\\^");
                offenderRemarks = offenderRemarksArray[0];
                try {
                    offenderRemarksChildArray = offenderRemarks.split("!");
                    if (offenderRemarksChildArray.length > 10) {
                        String remarks = offenderRemarksChildArray[10];
                        if (!remarks.isEmpty() && !"null".equals(remarks)) {
                            if (remarks.contains("NA")) {
                                mUiHelper.showToastShort(getResources().getString(R.string.no_vehicle_remarks_found));
                            } else if (!remarks.contains("NA")) {
                                if (remarks.contains(getResources().getString(R.string.sold_out_vehicle_flag))) {
                                    soldOutVehicle = true;
                                    showAlertDialog("" + getResources().getString(R.string.sold_out_vehicle_flag), "" + getResources().getString(R.string.sold_out_vehicle), "" + getResources().getString(R.string.sold_out_vehicle_message));
                                } else if (remarks.contains(getResources().getString(R.string.fake_vehicle_flag))) {
                                    String fakesChassisNo = offenderRemarksChildArray[8] != null && !"".equals(offenderRemarksChildArray[8].trim())
                                            ? offenderRemarksChildArray[8].substring(offenderRemarksChildArray[8].length() - 5) : "";
                                    Log.i(TAG, "fakesChassisNo-->" + fakesChassisNo);
                                    showAlertDialog("" + getResources().getString(R.string.fake_vehicle_flag), "" + getResources().getString(R.string.fake_vehicle), "" + getResources().getString(R.string.fake_vehicle_message));
                                } else {
                                    showAlertDialog("" + getResources().getString(R.string.remarks_flag), "" + getResources().getString(R.string.remarks), "" + remarks);
                                }
                            }
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                try {
                    theftVehicle = offenderRemarksArray[1];
                    if ("Y".equalsIgnoreCase(theftVehicle)) {
                        new VibratorUtils(getApplicationContext()).vibratePhone(10000);
                        AlertDialog.Builder builder = new AlertDialog.Builder(SpotChallanActivity.this);
                        builder.setTitle(getResources().getString(R.string.vehicle_remarks));
                        builder.setMessage(getResources().getString(R.string.it_is_theft_vehicle));
                        builder.setCancelable(false);
                        builder.setPositiveButton(SpotChallanActivity.this.getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                new VibratorUtils(getApplicationContext()).vibrateStopPhone();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
            if (Constants.DETAINED_ITEMS.equals(flag)) {
                String[] split = response.split("@");
                int length = split.length;
                String[][] splitChild = new String[length][];
                detainedItemsList = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    splitChild[i] = split[i].split(":");
                    DetainedItemsModel model = new DetainedItemsModel();
                    model.setWheelerCode(splitChild[i][0]);
                    model.setDetainedItem(splitChild[i][1]);
                    model.setChallanNo(splitChild[i][2]);
                    model.setLicenceNo(splitChild[i][3]);
                    model.setAadhaarNo(splitChild[i][4]);
                    model.setContactNo(splitChild[i][5]);
                    model.setDriverName(splitChild[i][6]);
                    model.setMinorFlagCode(splitChild[i][7]);
                    detainedItemsList.add(model);
                }
                for (int i = 0; i < detainedItemsList.size(); i++) {
                    DetainedItemsModel model = detainedItemsList.get(i);

                }
            }
        }
    }

    private void getPointNamesByPsCode(String psCd) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        SoapObject requestPointNames = new SoapObject("" + URLs.namespace, "" + URLs.getPointNamesByPsName);
        requestPointNames.addProperty(URLParams.psCode, "" + psCd);
        SoapController asyncTaskPointNames = new SoapController(this, requestPointNames,
                this, "" + Constants.POINT_NAMES);
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        asyncTaskPointNames.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void prepareViolationsDialog() {
        violationsSelectDialog.setMinSelectionLimit(0)
                .setMaxSelectionLimit(violationsList.size())
                .setViolationsList(violationsList)
                .setPreviouslySelectedIdsList(previouslySelectedIdsList)
                .onSubmit(new ViolationsSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                        if (ViolationsSelectDialog.selectedIdsForCallback.size() > 0) {
                            lnr_lyt_selected_violations_card_display.setVisibility(View.VISIBLE);
                        }
                        lnr_lyt_selected_violations.removeAllViews();
                        previouslySelectedIdsList = selectedIds;
                        selectedViolationsList = new ArrayList<>(selectedIds.size());

                        totalFineAmount = 0.0;
                        isSelectedWithoutDL = false;
                        detainedStatus = "0";

                        for (int i = 0; i < selectedIds.size(); i++) {
                            selectedViolationsList.add(violationsList.get(selectedIds.get(i)));
                            int offenceCode = 0;
                            try {
                                offenceCode = Integer.parseInt(selectedViolationsList.get(i).getOffenceCode());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            if (offenceCode == 64 || offenceCode == 123) {
                                isSelectedWithoutDL = true;
                                detainedStatus = "1";
                            }
                            chargeSheetStatus = selectedViolationsList.get(i).getChargeSheetStatus();
                            Log.i(TAG, "selectedOffenceCode-->" + offenceCode);
                            Log.i(TAG, "chargeSheetStatus-->" + chargeSheetStatus);

                            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View inflateView = inflater.inflate(R.layout.item_selected_violations, null);
                            AppCompatTextView tv_selected_violation_name = inflateView.findViewById(R.id.tv_selected_violation_name);
                            AppCompatTextView tv_selected_violation_amount = inflateView.findViewById(R.id.tv_selected_violation_amount);
                            AppCompatTextView tv_selected_violation_remove = inflateView.findViewById(R.id.tv_selected_violation_remove);
                            String totalViolation = (i + 1) + ". " + selectedViolationsList.get(i).getTotalViolation();
                            tv_selected_violation_name.setText(totalViolation);
                            Log.i(TAG, "totalViolation-->" + totalViolation);
                            String additionFineAmount = getResources().getString(R.string.indian_rupee)
                                    + Double.parseDouble(selectedViolationsList.get(i).getMinAmount());
                            tv_selected_violation_amount.setText(additionFineAmount);

                            lnr_lyt_selected_violations.addView(inflateView);

                            totalFineAmount = totalFineAmount + Double.parseDouble(selectedViolationsList.get(i).getMinAmount());
                            String s1 = getResources().getString(R.string.indian_rupee) + totalFineAmount;
                            tv_total_fine_amount_display.setText(s1);

                            final int index = i;

                            tv_selected_violation_remove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    lnr_lyt_selected_violations.removeView(inflateView);
                                    int cd = 0;
                                    try {
                                        cd = Integer.parseInt(selectedViolationsList.get(index).getOffenceCode());
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                    if (cd == 64 || cd == 123) {
                                        isSelectedWithoutDL = false;
                                        detainedStatus = "0";
                                    }
                                    totalFineAmount = totalFineAmount - Double.parseDouble(selectedViolationsList.get(index).getMinAmount());
                                    String subtractionFineAmount = getResources().getString(R.string.indian_rupee) + totalFineAmount;
                                    tv_total_fine_amount_display.setText(subtractionFineAmount);

                                    removeFromSelection(index);
                                }
                            });
                        }
                        spinner_violations.setText(getResources().getString(R.string.selected_violations));
                    }

                    @Override
                    public void onCancel() {
                        Log.d("onCancel", "Dialog cancelled");
                    }
                });
    }

    private void removeFromSelection(final int index) {
        for (int i = 0; i < selectedViolationsList.size(); i++) {
            if (selectedViolationsList.get(index).getId() == ViolationsSelectDialog.selectedIdsForCallback.get(i)) {
                ViolationsSelectDialog.selectedIdsForCallback.remove(i);
                break;
            }
        }
        if (ViolationsSelectDialog.selectedIdsForCallback.size() == 0) {
            lnr_lyt_selected_violations.removeAllViews();
            lnr_lyt_selected_violations_card_display.setVisibility(View.GONE);
            spinner_violations.setText(getResources().getString(R.string.select_violations));
        }
        if ("1".equals(selectedViolationsList.get(index).getChargeSheetStatus())) {
            chargeSheetStatus = "0";
            img_display.setImageDrawable(null);
            img_display.setVisibility(View.GONE);
            imgFromCameraGallery = "0";
        }
    }

    private void getOffenceDetailsByWheelerChallanTypeUnitRemark(String wheelerCd) {
        SoapObject soapObject = new SoapObject(URLs.namespace, URLs.getOffenceDetailsbyWheelerChallanTypeUnitRemark);
        soapObject.addProperty(URLParams.wheelerCode, wheelerCd);
        soapObject.addProperty(URLParams.challanType, "TE");
        soapObject.addProperty(URLParams.pidCode, pidCode);
        soapObject.addProperty(URLParams.remark, theftVehicle);

        SoapController soapController = new SoapController(this, soapObject, this, "" + Constants.VIOLATIONS);
        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
        soapController.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (CameraUtils.isDeviceSupportCamera(this)) {
                        mUiHelper.showToastLongCentre(getResources().getString(R.string.device_camera_doesnt_supports));
                    } else {
                        openCamera();
                    }
                } else {
                    mUiHelper.showToastLongCentre(getResources().getString(R.string.permission_denied));
                }
                break;
            case PermissionUtil.REQUEST_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    mUiHelper.showToastLongCentre(getResources().getString(R.string.permission_denied));
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //File file = CameraUtils.getOutputMediaFile(this, CameraUtils.MEDIA_TYPE_IMAGE);
        File file = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.temp_file_name).trim());
        imageStoragePath = file.getAbsolutePath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, CameraUtils.getOutputMediaFileUri(this, file));
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, CameraUtils.REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PENDING_CHALLANS) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String selectedListData = (String) bundle.getSerializable("selectedChallansData");
                    mUiHelper.showToastShort(selectedListData);
                    Log.i(TAG, "selectedChallansData-->" + selectedListData);
                }
            } else if (resultCode == RESULT_CANCELED) {
                mUiHelper.showToastShort(getResources().getString(R.string.you_not_selected_any_pending_challans));
            } else {
                mUiHelper.showToastShort(getResources().getString(R.string.you_not_selected_any_pending_challans));
            }
        } else if (requestCode == CameraUtils.REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                //CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                mUiHelper.showToastShort(getResources().getString(R.string.user_cancelled_image_capture));
            } else {
                mUiHelper.showToastShort(getResources().getString(R.string.failed_to_capture_image));
            }
        } else if (requestCode == CameraUtils.REQUEST_PICK_GALLERY) {
            if (resultCode == RESULT_OK) {
                //CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);
                attachFromGallery(data);
            } else if (resultCode == RESULT_CANCELED) {
                mUiHelper.showToastShort(getResources().getString(R.string.user_cancelled_attaching_image));
            } else {
                mUiHelper.showToastShort(getResources().getString(R.string.failed_attach_image));
            }
        } else if (requestCode == CameraUtils.REQUEST_VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);
                //previewVideo();
            } else if (resultCode == RESULT_CANCELED) {
                mUiHelper.showToastShort(getResources().getString(R.string.user_cancelled_video_recording));
            } else {
                mUiHelper.showToastShort(getResources().getString(R.string.failed_to_record_video));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void previewCapturedImage() {
        try {
            File tempFile = new File(String.valueOf(Environment.getExternalStorageDirectory()));
            for (File f : tempFile.listFiles()) {
                if (getResources().getString(R.string.temp_file_name).trim().equals(f.getName())) {
                    tempFile = f;
                    break;
                }
            }
            Bitmap decodedBitmap;
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            //bitmapOptions.inSampleSize = 10;
            decodedBitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), bitmapOptions);

            String pathName = Environment.getExternalStorageDirectory()
                    + File.separator + getResources().getString(R.string.app_name)
                    + File.separator + DateUtils.getTodaysDate();

            File folderName = new File(pathName);
            if (!folderName.exists()) {
                folderName.mkdirs();
            }
            tempFile.delete();

            OutputStream outputStream;

            File file = new File(pathName, System.currentTimeMillis() + ".jpg");

            try {
                outputStream = new FileOutputStream(file);

                Bitmap mutableBitmap = decodedBitmap.copy(Bitmap.Config.ARGB_8888, true);

                Canvas canvas = new Canvas(mutableBitmap);

                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);

                int xPos = (canvas.getWidth() / 2);
                int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.rotate(-90, xPos, yPos);
                canvas.drawText("Date & Time: " + DateUtils.getPresentDateTime(), xPos, yPos + 300, paint);
                if (mGpsTracker.canGetLocation()) {
                    String address = getAddressFromLatLng(mGpsTracker.getLatitude(), mGpsTracker.getLongitude());
                    canvas.drawText("Lat:" + mGpsTracker.getLatitude(), xPos, yPos + 400, paint);
                    canvas.drawText("Long:" + mGpsTracker.getLongitude(), xPos, yPos + 500, paint);
                }

                Display defaultDisplay = getWindowManager().getDefaultDisplay();

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(mutableBitmap, defaultDisplay.getWidth(), defaultDisplay.getHeight(), true);

                Matrix matrix = new Matrix();
                matrix.postRotate(90);

                mutableBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    imgFromCameraGallery = "0";
                }
                new SingleMediaScanner(this, file);
                img_display.setVisibility(View.VISIBLE);
                img_display.setImageBitmap(mutableBitmap);
                imgFromCameraGallery = "1";
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                imgFromCameraGallery = "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
            imgFromCameraGallery = "0";
        }
    }

    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, CameraUtils.REQUEST_PICK_GALLERY);
    }

    private void attachFromGallery(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        assert selectedImage != null;
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        Bitmap decodedBitmap = BitmapFactory.decodeFile(picturePath);

        img_display.setVisibility(View.VISIBLE);
        img_display.setImageBitmap(decodedBitmap);
        imgFromCameraGallery = "2";
    }

    private String getAddressFromLatLng(double lat, double lng) {
        String address = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null) {
                Address mAddress = addresses.get(0);
                StringBuilder addressBuilder = new StringBuilder();
                for (int i = 0; i <= mAddress.getMaxAddressLineIndex(); i++) {
                    addressBuilder.append(mAddress.getAddressLine(i)).append("\n");
                }
                address = addressBuilder.toString();
            } else {
                mUiHelper.showToastShortCentre(getResources().getString(R.string.no_address_returned));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

    private void initViews() {
        tv_challan_type_header = findViewById(R.id.tv_challan_type_header);
        switch (GlobalModel.challanType) {
            case Constants.SPOT:
                tv_challan_type_header.setText(getString(R.string.spot_challan));
                break;
            case Constants.DRUNK_DRIVE:
                tv_challan_type_header.setText(getString(R.string.drunk_drive));
                break;
            case Constants.CRANE:
                tv_challan_type_header.setText(getString(R.string._41_cp_act));
                break;
            default:
                break;
        }
        rg_challan_base = findViewById(R.id.rg_challan_base);

        spinner_ps_name = findViewById(R.id.spinner_ps_name);
        spinner_point_name = findViewById(R.id.spinner_point_name);
        spinner_resp_ps_name = findViewById(R.id.spinner_resp_ps_name);

        rb_regn_no = findViewById(R.id.rb_regn_no);
        rb_tr_no = findViewById(R.id.rb_tr_no);
        rb_engine_no = findViewById(R.id.rb_engine_no);
        rb_chassis_no = findViewById(R.id.rb_chassis_no);

        tv_vehicle_no_header = findViewById(R.id.tv_vehicle_no_header);

        tv_star_vehicle_no = findViewById(R.id.tv_star_vehicle_no);

        et_regn_no = findViewById(R.id.et_regn_no);
        et_regn_no.setText("TS08EG9748");
        et_dl_no = findViewById(R.id.et_dl_no);
        et_dl_no.setText("7461WGL1998OD");
        et_dl_dob = findViewById(R.id.et_dl_dob);
        et_dl_dob.setText("16-Apr-1980");

        img_dob_select = findViewById(R.id.img_dob_select);

        btn_get_rta_details = findViewById(R.id.btn_get_rta_details);

        lnr_lyt_rc_details = findViewById(R.id.lnr_lyt_rc_details);

        tv_regd_number = findViewById(R.id.tv_regd_number);
        tv_rc_regd_number = findViewById(R.id.tv_rc_regd_number);
        tv_rc_owner_name = findViewById(R.id.tv_rc_owner_name);
        tv_rc_owner_address = findViewById(R.id.tv_rc_owner_address);
        tv_rc_chassis_no = findViewById(R.id.tv_rc_chassis_no);
        tv_rc_engine_no = findViewById(R.id.tv_rc_engine_no);
        tv_rc_vehicle_type = findViewById(R.id.tv_rc_vehicle_type);
        tv_rc_insurance_validity = findViewById(R.id.tv_rc_insurance_validity);

        tv_rc_details_not_found = findViewById(R.id.tv_rc_details_not_found);

        lnr_lyt_dl_details = findViewById(R.id.lnr_lyt_dl_details);

        tv_dl_number = findViewById(R.id.tv_dl_number);
        tv_dl_name = findViewById(R.id.tv_dl_name);
        tv_dl_father_name = findViewById(R.id.tv_dl_father_name);
        tv_dl_age = findViewById(R.id.tv_dl_age);
        tv_dl_gender = findViewById(R.id.tv_dl_gender);
        tv_dl_address = findViewById(R.id.tv_dl_address);
        tv_dl_issued_on = findViewById(R.id.tv_dl_issued_on);
        tv_dl_penalty_points = findViewById(R.id.tv_dl_penalty_points);

        img_dl_img = findViewById(R.id.img_dl_img);

        tv_dl_details_not_found = findViewById(R.id.tv_dl_details_not_found);

        lnr_lyt_pending_challans = findViewById(R.id.lnr_lyt_pending_challans);

        tv_no_of_pending_challans = findViewById(R.id.tv_no_of_pending_challans);
        tv_total_pending_amount = findViewById(R.id.tv_total_pending_amount);

        tv_pending_challans_not_found = findViewById(R.id.tv_pending_challans_not_found);

        tv_total_fine_amount_display = findViewById(R.id.tv_total_fine_amount_display);

        lnr_lyt_driver_information = findViewById(R.id.lnr_lyt_driver_information);

        tv_star_driver = findViewById(R.id.tv_star_driver);

        et_driver_name = findViewById(R.id.et_driver_name);
        et_driver_father_name = findViewById(R.id.et_driver_father_name);
        et_driver_address = findViewById(R.id.et_driver_address);
        et_driver_age = findViewById(R.id.et_driver_age);

        spinner_wheeler_code = findViewById(R.id.spinner_wheeler_code);
        spinner_violations = findViewById(R.id.spinner_violations);

        lnr_lyt_bac_serial_no = findViewById(R.id.lnr_lyt_bac_serial_no);
        lnr_lyt_bac_serial_no.setVisibility(View.GONE);

        lnr_lyt_bac_reading = findViewById(R.id.lnr_lyt_bac_reading);
        lnr_lyt_bac_reading.setVisibility(View.GONE);

        lnr_lyt_camera_gallery = findViewById(R.id.lnr_lyt_camera_gallery);

        lnr_lyt_selected_violations_card_display = findViewById(R.id.lnr_lyt_selected_violations_card_display);

        lnr_lyt_selected_violations = findViewById(R.id.lnr_lyt_selected_violations);

        lnr_lyt_total_amount_display = findViewById(R.id.lnr_lyt_total_amount_display);

        lv_selected_violations = findViewById(R.id.lv_selected_violations);

        img_camera = findViewById(R.id.img_camera);
        img_gallery = findViewById(R.id.img_gallery);
        img_display = findViewById(R.id.img_display);

        lnr_lyt_btns = findViewById(R.id.lnr_lyt_btns);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_next = findViewById(R.id.btn_next);
    }

    private void initObjects() {
        mUiHelper = new UiHelper(this);
        mGpsTracker = new GPSTracker(this);
        mSharedPrefManager = SharedPrefManager.getInstance(this);
        loginModel = mSharedPrefManager.getOfficerDetails(SharedPrefManager.OFFICER_DETAILS_PREFS, LoginModel.class);
        pidCode = loginModel.getPidCode();
        unitCode = loginModel.getUnitCode();
        unitName = loginModel.getUnitName();
        psCode = loginModel.getPsCode().trim();
        Log.i(TAG, "pidCode-->" + pidCode);
        Log.i(TAG, "unitCode-->" + unitCode);
        Log.i(TAG, "unitName-->" + unitName);
        Log.i(TAG, "psCode-->" + psCode);
        violationsSelectDialog = new ViolationsSelectDialog();
    }

    private void setListeners() {
        spinner_ps_name.setOnClickListener(this);
        spinner_point_name.setOnClickListener(this);
        spinner_resp_ps_name.setOnClickListener(this);
        rg_challan_base.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_regn_no:
                        tv_vehicle_no_header.setText(getString(R.string.regn_no));
                        et_regn_no.setText("");
                        et_regn_no.setHint(getString(R.string.enter_vehicle_no));
                        challanBaseCd = "1";
                        break;
                    case R.id.rb_tr_no:
                        tv_vehicle_no_header.setText(getString(R.string.t_r_no));
                        et_regn_no.setText("");
                        et_regn_no.setHint(getString(R.string.enter_tr_no));
                        challanBaseCd = "2";
                        break;
                    case R.id.rb_engine_no:
                        tv_vehicle_no_header.setText(getString(R.string.engine_no));
                        et_regn_no.setText("");
                        et_regn_no.setHint(getString(R.string.enter_engine_no));
                        challanBaseCd = "3";
                        break;
                    case R.id.rb_chassis_no:
                        tv_vehicle_no_header.setText(getString(R.string.chassis_no));
                        et_regn_no.setText("");
                        et_regn_no.setHint(getString(R.string.enter_chassis_no));
                        challanBaseCd = "4";
                        break;
                    default:
                        break;
                }
            }
        });
        img_dob_select.setOnClickListener(this);
        btn_get_rta_details.setOnClickListener(this);
        spinner_wheeler_code.setOnClickListener(this);
        spinner_violations.setOnClickListener(this);
        lnr_lyt_pending_challans.setOnClickListener(this);
        img_camera.setOnClickListener(this);
        img_gallery.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    private void restoreFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CameraUtils.KEY_IMAGE_PATH)) {
                imageStoragePath = savedInstanceState.getString(CameraUtils.KEY_IMAGE_PATH);
                if (!TextUtils.isEmpty(imageStoragePath)) {
                    if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals(".jpg")) {
                        previewCapturedImage();
                        //} else if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals(".mp4")) {
                        //previewVideo();
                    }
                }
            }
        }
    }

    private void getWheelerDetailsFromDataBase() {
        DBFunctions dbFunctions = DBFunctions.getInstance(this);
        Cursor cursor = dbFunctions.getWheelerMaster();
        wheelerNamesHashMap = new HashMap<>();
        wheelerCodesArray = new String[cursor.getCount()];
        wheelerNamesArray = new String[cursor.getCount()];
        if (cursor.moveToFirst()) {
            int count = 0;
            do {
                String wheelerCode1 = cursor.getString(0);
                String wheelerName1 = cursor.getString(1);
                Log.i(Constants.APP_LOG, TAG + wheelerCode1);
                Log.i(Constants.APP_LOG, TAG + wheelerName1);
                wheelerCodesArray[count] = wheelerCode1;
                wheelerNamesArray[count] = wheelerName1;
                wheelerNamesHashMap.put(wheelerCode1, wheelerName1);
                count++;
            } while (cursor.moveToNext());
        }
    }

    private void showAlertDialog(final String flag, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SpotChallanActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mUiHelper.showToastShort(getResources().getString(R.string.please_click_on_cancel_button));
    }
}
