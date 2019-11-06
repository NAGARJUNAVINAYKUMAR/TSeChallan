package com.tspolice.echallan.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tspolice.echallan.activities.LoginActivity;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "TSeChallanSharedPrefs";
    public static final String OFFICER_DETAILS_PREFS = "OFFICER_DETAILS_PREFS";

    @SuppressLint("StaticFieldLeak")
    private static SharedPrefManager mInstance;
    private Context mContext;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    private SharedPrefManager(Context context) {
        mContext = context;
        mPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        mPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return mPreferences.getString(key, "");
    }

    public void removeString(String s) {
        mPreferences.edit().remove(s).apply();
    }

    public void putBoolean(String key, boolean value) {
        mPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return mPreferences.getBoolean(key, false);
    }

    public void removeBoolean(String s) {
        mPreferences.edit().remove(s).apply();
    }

    public void putInteger(String key, int value) {
        mPreferences.edit().putInt(key, value).apply();
    }

    public int getInteger(String key) {
        return mPreferences.getInt(key, 0);
    }

    public void removeInteger(String s) {
        mPreferences.edit().remove(s).apply();
    }

    public void clearPreferences() {
        mPreferences.edit().clear().apply();
        mContext.startActivity(new Intent(mContext, LoginActivity.class));
    }

    public void saveOfficerDetails(String key, Object object) {
        mPreferences.edit().putString(key, new Gson().toJson(object)).apply();
    }

    public <GenericClass> GenericClass getOfficerDetails(String key, Class<GenericClass> classType) {
        if (mPreferences.contains(key)) {
            return new Gson().fromJson(mPreferences.getString(key, ""), classType);
        }
        return null;
    }
}
