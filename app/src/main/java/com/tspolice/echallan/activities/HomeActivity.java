package com.tspolice.echallan.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.tspolice.echallan.R;
import com.tspolice.echallan.models.GlobalModel;
import com.tspolice.echallan.utils.Constants;
import com.tspolice.echallan.utils.UiHelper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private AppCompatImageView img_nav_home, img_spot, img_drunk_drive, img_crane, img_ghmc, img_seizure, img_settings;
    private RelativeLayout rel_lyt_content_home;
    private DrawerLayout drawer;
    private UiHelper mUiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initViews();

        initObjects();

        setListeners();

        rel_lyt_content_home.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_enter));

        GlobalModel.challanType = "";
    }

    private void initViews() {
        rel_lyt_content_home = findViewById(R.id.rel_lyt_content_home);

        img_nav_home = findViewById(R.id.img_nav_home);

        drawer = findViewById(R.id.drawer_layout);

        img_spot = findViewById(R.id.img_spot);
        img_drunk_drive = findViewById(R.id.img_drunk_drive);
        img_crane = findViewById(R.id.img_crane);
        img_ghmc = findViewById(R.id.img_ghmc);
        img_seizure = findViewById(R.id.img_seizure);
        img_settings = findViewById(R.id.img_settings);
    }

    private void initObjects() {
        mUiHelper = new UiHelper(this);
    }

    private void setListeners() {
        img_spot.setOnClickListener(this);
        img_drunk_drive.setOnClickListener(this);
        img_crane.setOnClickListener(this);
        img_settings.setOnClickListener(this);
        img_ghmc.setOnClickListener(this);
        img_seizure.setOnClickListener(this);
        img_nav_home.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            //super.onBackPressed();
            mUiHelper.alertDialogOkCancel(getString(R.string.do_you_want_logout_the_application), false, Constants.HOME);
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {
        } else if (id == R.id.nav_slideshow) {
        } else if (id == R.id.nav_manage) {
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        }
        //DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_spot:
                GlobalModel.challanType = "22";
                mUiHelper.intent(SpotChallanActivity.class);
                break;
            case R.id.img_drunk_drive:
                GlobalModel.challanType = "23";
                mUiHelper.intent(SpotChallanActivity.class);
                break;
            case R.id.img_crane:
                GlobalModel.challanType = "24";
                mUiHelper.intent(SpotChallanActivity.class);
                break;
            case R.id.img_settings:
                mUiHelper.intent(SettingsActivity.class);
                break;
            case R.id.img_ghmc:
                mUiHelper.intent(GhmcSeizureActivity.class);
                break;
            case R.id.img_seizure:
                mUiHelper.intent(GhmcSeizureActivity.class);
                break;
            case R.id.img_nav_home:
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
                break;
            default:
                break;
        }
    }
}
