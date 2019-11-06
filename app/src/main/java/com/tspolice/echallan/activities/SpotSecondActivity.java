package com.tspolice.echallan.activities;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.tspolice.echallan.R;
import com.tspolice.echallan.models.GlobalModel;
import com.tspolice.echallan.utils.Constants;
import com.tspolice.echallan.utils.UiHelper;

public class SpotSecondActivity extends AppCompatActivity implements
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private AppCompatTextView tv_spot_second_screen;
    private ToggleButton toggle_btn_vehicle, toggle_btn_rc, toggle_btn_licence, toggle_btn_permit, toggle_btn_none;
    private AppCompatButton btn_send_otp;
    final boolean detainStatus = true;
    private UiHelper mUiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_second);

        initViews();

        initObjects();

        setListeners();

        switch (GlobalModel.challanType) {
            case Constants.SPOT:
                tv_spot_second_screen.setText(getString(R.string.spot_challan));
                break;
            case Constants.DRUNK_DRIVE:
                tv_spot_second_screen.setText(getString(R.string.drunk_drive));
                break;
            default:
                tv_spot_second_screen.setText(getString(R.string._41_cp_act));
                break;
        }

        if (detainStatus) {
            toggle_btn_vehicle.setChecked(true);
            toggle_btn_vehicle.setEnabled(true);
        } else {
            toggle_btn_vehicle.setChecked(false);
            toggle_btn_vehicle.setEnabled(false);
        }
    }

    private void initViews() {
        tv_spot_second_screen = findViewById(R.id.tv_spot_second_screen);
        toggle_btn_vehicle = findViewById(R.id.toggle_btn_vehicle);
        toggle_btn_rc = findViewById(R.id.toggle_btn_rc);
        toggle_btn_licence = findViewById(R.id.toggle_btn_licence);
        toggle_btn_permit = findViewById(R.id.toggle_btn_permit);
        toggle_btn_none = findViewById(R.id.toggle_btn_none);

        btn_send_otp = findViewById(R.id.btn_send_otp);
    }

    private void initObjects() {
        //Context mContext = getApplicationContext();
        //Activity mActivity = SpotSecondActivity.this;
        mUiHelper = new UiHelper(this);
    }

    private void setListeners() {
        toggle_btn_vehicle.setOnCheckedChangeListener(this);
        toggle_btn_rc.setOnCheckedChangeListener(this);
        toggle_btn_licence.setOnCheckedChangeListener(this);
        toggle_btn_permit.setOnCheckedChangeListener(this);
        toggle_btn_none.setOnCheckedChangeListener(this);

        btn_send_otp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_otp:
                AlertDialog.Builder builder = new AlertDialog.Builder(SpotSecondActivity.this);
                LayoutInflater inflater = LayoutInflater.from(SpotSecondActivity.this);
                @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_otp_input, null);
                builder.setView(view);
                builder.setCancelable(true);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            default:
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.toggle_btn_vehicle:
                if (isChecked) {
                    toggle_btn_vehicle.setTextColor(getResources().getColor(R.color.colorWhite));
                    toggle_btn_vehicle.setBackground(getResources().getDrawable(R.drawable.toggle_btn_style_checked));
                    toggle_btn_vehicle.setCompoundDrawablesWithIntrinsicBounds(
                            getResources().getDrawable(R.drawable.ic_vehicle_detain_white), null, null, null);
                } else {
                    toggle_btn_vehicle.setTextColor(getResources().getColor(R.color.colorToggleCentre));
                    toggle_btn_vehicle.setBackground(getResources().getDrawable(R.drawable.toggle_btn_style_unchecked));
                    toggle_btn_vehicle.setCompoundDrawablesWithIntrinsicBounds(
                            getResources().getDrawable(R.drawable.ic_vehicle_detain_black), null, null, null);
                }
                break;
            case R.id.toggle_btn_rc:
                if (isChecked) {
                    toggle_btn_rc.setTextColor(getResources().getColor(R.color.colorWhite));
                    toggle_btn_rc.setBackground(getResources().getDrawable(R.drawable.toggle_btn_style_checked));
                    toggle_btn_rc.setCompoundDrawablesWithIntrinsicBounds(
                            getResources().getDrawable(R.drawable.ic_dl_detain_white), null, null, null);
                } else {
                    toggle_btn_rc.setTextColor(getResources().getColor(R.color.colorToggleCentre));
                    toggle_btn_rc.setBackground(getResources().getDrawable(R.drawable.toggle_btn_style_unchecked));
                    toggle_btn_rc.setCompoundDrawablesWithIntrinsicBounds(
                            getResources().getDrawable(R.drawable.ic_dl_detain_black), null, null, null);
                }
                break;
            case R.id.toggle_btn_licence:
                if (isChecked) {
                    toggle_btn_licence.setTextColor(getResources().getColor(R.color.colorWhite));
                    toggle_btn_licence.setBackground(getResources().getDrawable(R.drawable.toggle_btn_style_checked));
                    toggle_btn_licence.setCompoundDrawablesWithIntrinsicBounds(
                            getResources().getDrawable(R.drawable.ic_dl_detain_white), null, null, null);
                } else {
                    toggle_btn_licence.setTextColor(getResources().getColor(R.color.colorToggleCentre));
                    toggle_btn_licence.setBackground(getResources().getDrawable(R.drawable.toggle_btn_style_unchecked));
                    toggle_btn_licence.setCompoundDrawablesWithIntrinsicBounds(
                            getResources().getDrawable(R.drawable.ic_dl_detain_black), null, null, null);
                }
                break;
            case R.id.toggle_btn_permit:
                if (isChecked) {
                    toggle_btn_permit.setTextColor(getResources().getColor(R.color.colorWhite));
                    toggle_btn_permit.setBackground(getResources().getDrawable(R.drawable.toggle_btn_style_checked));
                    toggle_btn_permit.setCompoundDrawablesWithIntrinsicBounds(
                            getResources().getDrawable(R.drawable.ic_permit_detain_white), null, null, null);
                } else {
                    toggle_btn_permit.setTextColor(getResources().getColor(R.color.colorToggleCentre));
                    toggle_btn_permit.setBackground(getResources().getDrawable(R.drawable.toggle_btn_style_unchecked));
                    toggle_btn_permit.setCompoundDrawablesWithIntrinsicBounds(
                            getResources().getDrawable(R.drawable.ic_permit_detain_black), null, null, null);
                }
                break;
            case R.id.toggle_btn_none:
                if (isChecked) {
                    toggle_btn_none.setTextColor(getResources().getColor(R.color.colorWhite));
                    toggle_btn_none.setBackground(getResources().getDrawable(R.drawable.toggle_btn_style_checked));
                    toggle_btn_none.setCompoundDrawablesWithIntrinsicBounds(
                            getResources().getDrawable(R.drawable.ic_none_detain_white), null, null, null);

                    toggle_btn_vehicle.setTextColor(getResources().getColor(R.color.colorToggleCentre));
                    toggle_btn_vehicle.setBackground(getResources().getDrawable(R.drawable.toggle_btn_style_unchecked));
                    toggle_btn_vehicle.setCompoundDrawablesWithIntrinsicBounds(
                            getResources().getDrawable(R.drawable.ic_vehicle_detain_black), null, null, null);
                    toggle_btn_vehicle.setChecked(false);
                    toggle_btn_vehicle.setEnabled(false);

                    toggle_btn_rc.setTextColor(getResources().getColor(R.color.colorToggleCentre));
                    toggle_btn_rc.setBackground(getResources().getDrawable(R.drawable.toggle_btn_style_unchecked));
                    toggle_btn_rc.setCompoundDrawablesWithIntrinsicBounds(
                            getResources().getDrawable(R.drawable.ic_dl_detain_black), null, null, null);
                    toggle_btn_rc.setChecked(false);
                    toggle_btn_rc.setEnabled(false);

                    toggle_btn_licence.setTextColor(getResources().getColor(R.color.colorToggleCentre));
                    toggle_btn_licence.setBackground(getResources().getDrawable(R.drawable.toggle_btn_style_unchecked));
                    toggle_btn_licence.setCompoundDrawablesWithIntrinsicBounds(
                            getResources().getDrawable(R.drawable.ic_dl_detain_black), null, null, null);
                    toggle_btn_licence.setChecked(false);
                    toggle_btn_licence.setEnabled(false);

                    toggle_btn_permit.setTextColor(getResources().getColor(R.color.colorToggleCentre));
                    toggle_btn_permit.setBackground(getResources().getDrawable(R.drawable.toggle_btn_style_unchecked));
                    toggle_btn_permit.setCompoundDrawablesWithIntrinsicBounds(
                            getResources().getDrawable(R.drawable.ic_permit_detain_black), null, null, null);
                    toggle_btn_permit.setChecked(false);
                    toggle_btn_permit.setEnabled(false);
                } else {
                    toggle_btn_none.setTextColor(getResources().getColor(R.color.colorToggleCentre));
                    toggle_btn_none.setBackground(getResources().getDrawable(R.drawable.toggle_btn_style_unchecked));
                    toggle_btn_none.setCompoundDrawablesWithIntrinsicBounds(
                            getResources().getDrawable(R.drawable.ic_none_detain_black), null, null, null);
                    toggle_btn_vehicle.setEnabled(true);
                    toggle_btn_rc.setEnabled(true);
                    toggle_btn_licence.setEnabled(true);
                    toggle_btn_permit.setEnabled(true);
                }
                break;
            default:
                break;
        }
    }
}
