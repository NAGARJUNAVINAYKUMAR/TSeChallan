package com.tspolice.echallan.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatEditText;

import java.util.Objects;
import java.util.regex.Pattern;

public class ValidationUtils {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isEmpty(AppCompatEditText editText) {
        return Objects.requireNonNull(editText.getText()).toString().trim().isEmpty();
    }

    public static boolean isValidMobile(String mobileNo) {
        boolean check = false;
        if (!Pattern.matches(Constants.phoneRegex, mobileNo)) {
            if (mobileNo.length() == 10) {
                check = ((mobileNo.charAt(0) == '6')) || ((mobileNo.charAt(0) == '7')) || ((mobileNo.charAt(0) == '8'))
                        || ((mobileNo.charAt(0) == '9'));
            }
        }
        return check;
    }
}
