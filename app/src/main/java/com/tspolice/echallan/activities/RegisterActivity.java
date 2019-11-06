package com.tspolice.echallan.activities;

import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.tspolice.echallan.R;
import com.tspolice.echallan.models.restful.CadreModel;
import com.tspolice.echallan.models.restful.PsMasterModel;
import com.tspolice.echallan.models.restful.UnitMasterModel;
import com.tspolice.echallan.searchablespinner.SearchableSpinner;
import com.tspolice.echallan.utils.Constants;
import com.tspolice.echallan.utils.UiHelper;
import com.tspolice.echallan.utils.ValidationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements
        View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private static final String TAG = "RegisterActivity-->";
    AppCompatTextView tv_user_registration, tv_already_have_an_account, tv_login_here;
    private AppCompatEditText et_first_name, et_last_name, et_email_id, et_contact_no,
            et_emp_id, et_emp_pao_no;
    private SearchableSpinner spinner_unit_name, spinner_ps_name, spinner_cadre_name;
    private AppCompatImageView img_next;
    private UiHelper mUiHelper;
    List<UnitMasterModel> unitMasterList;
    List<PsMasterModel> psMasterList;
    List<CadreModel> cadreMasterList;
    List<String> unitNamesList, psNamesList, cadreNamesList;
    private HashMap<String, String> unitNamesHashMap, psNamesHashMap, cadreNamesHashMap;
    String unitName, unitCode, psName, psCode, cadreName, cadreCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();

        initObjects();

        setTypefaces();

        loadUnitNames();

        loadCadreNames();

        setListeners();
    }

    private void initViews() {
        tv_user_registration = findViewById(R.id.tv_user_registration);
        et_first_name = findViewById(R.id.et_first_name);
        et_last_name = findViewById(R.id.et_last_name);
        et_email_id = findViewById(R.id.et_email_id);
        et_contact_no = findViewById(R.id.et_contact_no);
        et_emp_id = findViewById(R.id.et_emp_id);
        et_emp_pao_no = findViewById(R.id.et_emp_pao_no);
        spinner_unit_name = findViewById(R.id.spinner_unit_name);
        spinner_ps_name = findViewById(R.id.spinner_ps_name);
        spinner_cadre_name = findViewById(R.id.spinner_cadre_name);
        tv_already_have_an_account = findViewById(R.id.tv_already_have_an_account);
        tv_login_here = findViewById(R.id.tv_login_here);
        img_next = findViewById(R.id.img_next);
    }

    private void initObjects() {
        mUiHelper = new UiHelper(this);
    }

    private void setTypefaces() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/cambriab.ttf");
        tv_user_registration.setTypeface(typeface);
        spinner_unit_name.setSelection(0);
        spinner_ps_name.setSelection(0);
        spinner_cadre_name.setSelection(0);
    }

    private void loadUnitNames() {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), true);
        try {
            String unitMaster = mUiHelper.loadJSONFromAssets("unitMaster.json");
            if (unitMaster != null && !"".equals(unitMaster)) {
                JSONObject jsonObject = new JSONObject(unitMaster);
                if (jsonObject.getInt("respCode") == 1) {
                    Log.i(TAG, jsonObject.getString("respDesc"));
                    JSONArray jsonArray = jsonObject.getJSONArray("respRemark");
                    unitMasterList = new ArrayList<>(jsonArray.length());
                    unitNamesHashMap = new HashMap<>(jsonArray.length());
                    unitNamesList = new ArrayList<>(jsonArray.length());
                    unitNamesList.add(Constants.selectUnitName);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        UnitMasterModel unitMasterModel = new UnitMasterModel();
                        unitMasterModel.setUnitName(jsonObject1.getString("unit_name") != null ? jsonObject1.getString("unit_name") : "");
                        unitMasterModel.setUnitCode(jsonObject1.getInt("unit_cd"));
                        unitMasterList.add(unitMasterModel);
                        unitNamesList.add(unitMasterModel.getUnitName());
                        unitNamesHashMap.put(unitMasterModel.getUnitName(), String.valueOf(unitMasterModel.getUnitCode()));
                    }
                    mUiHelper.dismissProgressDialog();
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitNamesList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_unit_name.setAdapter(arrayAdapter);
                } else {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.try_again));
                }
            } else {
                mUiHelper.showToastShort(getResources().getString(R.string.empty_response));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mUiHelper.dismissProgressDialog();
            mUiHelper.showToastShort(getResources().getString(R.string.something_went_wrong));
        }
    }

    private void loadCadreNames() {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), true);
        try {
            String cadreMaster = mUiHelper.loadJSONFromAssets("cadreMaster.json");
            if (cadreMaster != null && !"".equals(cadreMaster)) {
                JSONObject jsonObject = new JSONObject(cadreMaster);
                if (jsonObject.getInt("respCode") == 1) {
                    Log.i(TAG, jsonObject.getString("respDesc"));
                    JSONArray jsonArray = jsonObject.getJSONArray("respRemark");
                    cadreMasterList = new ArrayList<>(jsonArray.length());
                    cadreNamesList = new ArrayList<>(jsonArray.length());
                    cadreNamesHashMap = new HashMap<>(jsonArray.length());
                    cadreNamesList.add(Constants.selectCadreName);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        CadreModel cadreModel = new CadreModel();
                        cadreModel.setCadreName(jsonObject1.getString("cadre_name") != null ? jsonObject1.getString("cadre_name") : "");
                        cadreModel.setCadreCode(jsonObject1.getInt("cadre_cd") > 0 ? jsonObject1.getInt("cadre_cd") : 0);
                        cadreModel.setCadreSF(jsonObject1.getString("cadre_sf") != null ? jsonObject1.getString("cadre_sf") : "");
                        cadreMasterList.add(cadreModel);
                        cadreNamesList.add(cadreModel.getCadreName());
                        cadreNamesHashMap.put(cadreModel.getCadreName(), String.valueOf(cadreModel.getCadreCode()));
                    }
                    mUiHelper.dismissProgressDialog();
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cadreNamesList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_cadre_name.setAdapter(arrayAdapter);
                } else {
                    mUiHelper.showToastShortCentre(getResources().getString(R.string.try_again));
                }
            } else {
                mUiHelper.showToastShort(getResources().getString(R.string.empty_response));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mUiHelper.dismissProgressDialog();
            mUiHelper.showToastShort(getResources().getString(R.string.something_went_wrong));
        }
    }

    private void setListeners() {
        spinner_unit_name.setOnItemSelectedListener(this);
        spinner_ps_name.setOnItemSelectedListener(this);
        spinner_cadre_name.setOnItemSelectedListener(this);
        img_next.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_next:
                String firstName = Objects.requireNonNull(et_first_name.getText()).toString().trim();
                String lastName = Objects.requireNonNull(et_last_name.getText()).toString().trim();
                String emailId = Objects.requireNonNull(et_email_id.getText()).toString().trim();
                String contactNo = Objects.requireNonNull(et_contact_no.getText()).toString().trim();
                String empId = Objects.requireNonNull(et_emp_id.getText()).toString().trim();
                String empPaoNo = Objects.requireNonNull(et_emp_pao_no.getText()).toString().trim();
                if (firstName.isEmpty()) {
                    et_first_name.requestFocus();
                    mUiHelper.showToastShortCentre(getString(R.string.first_name_is_required));
                } else if (lastName.isEmpty()) {
                    et_last_name.requestFocus();
                    mUiHelper.showToastShortCentre(getString(R.string.last_name_is_required));
                } else if (emailId.isEmpty()) {
                    et_email_id.requestFocus();
                    mUiHelper.showToastShortCentre(getString(R.string.email_id_is_required));
                } else if (contactNo.isEmpty()) {
                    et_contact_no.requestFocus();
                    mUiHelper.showToastShortCentre(getString(R.string.contact_no_is_required));
                } else if (!ValidationUtils.isValidMobile(contactNo)) {
                    et_contact_no.requestFocus();
                    mUiHelper.showToastShortCentre(getString(R.string.enter_valid_mobile_no));
                } else if (empId.isEmpty()) {
                    et_emp_id.requestFocus();
                    mUiHelper.showToastShortCentre(getString(R.string.emp_id_is_required));
                } else if (empPaoNo.isEmpty()) {
                    et_emp_pao_no.requestFocus();
                    mUiHelper.showToastShortCentre(getString(R.string.employee_pao_no_is_required));
                } else {
                    mUiHelper.showToastShortCentre(getString(R.string.submit));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_unit_name:
                unitName = parent.getSelectedItem().toString();
                for (String mapUnitName : unitNamesHashMap.keySet()) {
                    if (unitName.equals(mapUnitName)) {
                        unitCode = unitNamesHashMap.get(mapUnitName);
                        mUiHelper.showToastShort(unitName + " (" + unitCode + ") selected");
                        Log.i(TAG, "unitName, unitCode-->" + unitName + ", " + unitCode);
                        if (psNamesList != null) {
                            psNamesList.clear();
                        }
                        spinner_ps_name.setSelection(0);
                        psMasterbyUnit(unitCode);
                        if (cadreNamesList != null) {
                            cadreNamesList.clear();
                        }
                        spinner_cadre_name.setSelection(0);
                        break;
                    }
                }
                break;
            case R.id.spinner_ps_name:
                psName = parent.getSelectedItem().toString();
                for (String mapPsName : psNamesHashMap.keySet()) {
                    if (psName.equals(mapPsName)) {
                        psCode = psNamesHashMap.get(mapPsName);
                        mUiHelper.showToastShort(psName + " (" + psCode + ") selected");
                        Log.i(TAG, "psName, psCode-->" + psName + ", " + psCode);
                        break;
                    }
                }
                break;
            case R.id.spinner_cadre_name:
                cadreName = parent.getSelectedItem().toString();
                for (String mapCadreName : cadreNamesHashMap.keySet()) {
                    if (cadreName.equals(mapCadreName)) {
                        cadreCode = cadreNamesHashMap.get(mapCadreName);
                        mUiHelper.showToastShort(cadreName + " (" + cadreCode + ") selected");
                        Log.i(TAG, "cadreName, cadreCode-->" + cadreName + ", " + cadreCode);
                        break;
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void psMasterbyUnit(String unitCode) {
        mUiHelper.showProgressDialog(getResources().getString(R.string.please_wait), true);
        try {
            String psMaster = mUiHelper.loadJSONFromAssets("psMasterbyUnit.json");
            if (psMaster != null && !"".equals(psMaster)) {
                JSONObject jsonObject = new JSONObject(psMaster);
                if (jsonObject.getInt("respCode") == 1) {
                    Log.i(TAG, jsonObject.getString("respDesc"));
                    JSONArray jsonArray = jsonObject.getJSONArray("respRemark");
                    psMasterList = new ArrayList<>(jsonArray.length());
                    psNamesList = new ArrayList<>(jsonArray.length());
                    psNamesHashMap = new HashMap<>(jsonArray.length());
                    psNamesList.add(Constants.selectPsName);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        PsMasterModel psMasterModel = new PsMasterModel();
                        psMasterModel.setPsName(jsonObject1.getString("ps_name") != null ? jsonObject1.getString("ps_name") : "");
                        psMasterModel.setPsCode(jsonObject1.getInt("psCode") > 0 ? jsonObject1.getInt("psCode") : 0);
                        psMasterList.add(psMasterModel);
                        psNamesList.add(psMasterModel.getPsName());
                        unitNamesHashMap.put(psMasterModel.getPsName(), String.valueOf(psMasterModel.getPsCode()));
                    }
                    mUiHelper.dismissProgressDialog();
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, psNamesList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_ps_name.setAdapter(arrayAdapter);
                }
            } else {
                mUiHelper.showToastShort(getResources().getString(R.string.empty_response));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mUiHelper.dismissProgressDialog();
            mUiHelper.showToastShort(getResources().getString(R.string.something_went_wrong));
        }
    }
}
