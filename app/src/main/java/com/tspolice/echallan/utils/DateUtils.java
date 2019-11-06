package com.tspolice.echallan.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static SimpleDateFormat simpleDateFormat, format;
    private static final String TAG = "DateUtils-->";

    public static String getPresentDateTime() {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
        return simpleDateFormat.format(new Date());
    }

    public static String getTodayDate() {
        return new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTodaysDate() {
        format = new SimpleDateFormat("dd-MMM-yyyy");
        return format.format(new Date());
    }

    @SuppressLint("SimpleDateFormat")
    public String getPresentTime() {
        String pattern = "HH:mm";
        format = new SimpleDateFormat(pattern);
        return format.format(new Date());
    }

    static String getTimeStamp() {
        // Preparing media file naming convention and adds timestamp
        return new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
    }

    public static String getDateFormat(String date) throws ParseException {
        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        return simpleDateFormat.format(new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(date));
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public static String addYear(int no_of_years) {
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println(simpleDateFormat.format(date.getTime()));
        date.add(Calendar.YEAR, no_of_years);
        System.out.println(simpleDateFormat.format(date.getTime()));
        return simpleDateFormat.format(date.getTime());
    }

    public static Date addSeconds(int no_of_seconds) {
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        date.add(Calendar.SECOND, no_of_seconds);
        return date.getTime();
    }

    public static String LeftPad(String Str, int length) {
        return String.format("%0" + length + "d", Str);
    }

    public static String padLeft(String str, int length, String padChar) {
        StringBuilder pad = new StringBuilder();
        for (int i = 0; i < length; i++) {
            pad.append(padChar);
        }
        return pad.substring(str.length()) + str;
    }

    public static String padRight(String str, int length, String padChar) {
        StringBuilder pad = new StringBuilder();
        for (int i = 0; i < length; i++) {
            pad.append(padChar);
        }
        return str + pad.substring(str.length());
    }

    public static String getHours() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        return String.valueOf(hours);
    }

    public static String getDayofYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.DAY_OF_YEAR);
        return String.valueOf(year);
    }

    public static String getYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        return String.valueOf(year);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDate(String dt) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        String formattedDate = "";
        try {
            date = formatter.parse(dt);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        try {
            if (date == null) {
                formatter = new SimpleDateFormat("yyyy-MM-dd");
                date = formatter.parse(dt);
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        formatter = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = formatter.format(date);
        return formattedDate;
    }

    public static long daysCalculate(String dateStart, String dateStop) {
        // String dateStart ="0101120912";
        // String dateStop = "0101121041";
        long min = 0;
        long diffDays = 0;
        // HH converts hour in 24 hours format (0-23), day calculation
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();
            Log.i(TAG, "diff--> " + diff);
            long diffSeconds = diff / 1000 % 60;
            Log.i(TAG, "diffSeconds--> " + diffSeconds);
            long diffMinutes = diff / (60 * 1000) % 60;
            Log.i(TAG, "diffMinutes--> " + diffMinutes);
            long diffHours = diff / (60 * 60 * 1000) % 24;
            Log.i(TAG, "diffHours--> " + diffHours);
            diffDays = diff / (24 * 60 * 60 * 1000);

            //   min = (diffDays * 24 * 60) + (diffHours * 60) + (diffMinutes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diffDays;
    }

    public static String getAge(int day, int month, int year) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        int ageInt = age;
        return Integer.toString(ageInt);
    }
}
