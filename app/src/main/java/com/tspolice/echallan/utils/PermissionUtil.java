package com.tspolice.echallan.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.tspolice.echallan.R;

import java.util.ArrayList;

public class PermissionUtil {

    private static final int REQUEST_READ_PHONE_STATE = 101;
    private static final int REQUEST_COARSE_LOCATION = 102;
    private static final int REQUEST_FINE_LOCATION = 103;
    public static final int REQUEST_CAMERA = 104;
    public static final int REQUEST_STORAGE = 105;
    public static final int REQUEST_GROUP_PERMISSIONS = 109;

    public static final int INT_READ_PHONE_STATE = 1;
    public static final int INT_COARSE_LOCATION = 2;
    public static final int INT_FINE_LOCATION = 3;
    public static final int INT_CAMERA = 4;
    public static final int INT_STORAGE = 5;
    public static final int INT_GROUP_PERMISSIONS = 9;

    public static void requestAllPermissions(final Context context) {
        ArrayList<String> permissionsNeeded = new ArrayList<>();
        ArrayList<String> permissionsAvailable = new ArrayList<>();
        permissionsAvailable.add(Manifest.permission.READ_PHONE_STATE);
        permissionsAvailable.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsAvailable.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsAvailable.add(Manifest.permission.CAMERA);
        permissionsAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        for (String permission : permissionsAvailable) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(permission);
            }
        }
        requestGroupPermissions(context, permissionsNeeded);
    }

    private static void requestGroupPermissions(final Context context, ArrayList<String> permissionsNeeded) {
        String[] permissions = new String[permissionsNeeded.size()];
        permissionsNeeded.toArray(permissions);
        ActivityCompat.requestPermissions((Activity) context, permissions, REQUEST_GROUP_PERMISSIONS);
    }

    public static int checkPermission(final Context context, int permission) {
        int permissionStatus = PackageManager.PERMISSION_DENIED;
        switch (permission) {
            case INT_READ_PHONE_STATE:
                permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
                break;
            case INT_FINE_LOCATION:
                permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
                break;
            case INT_COARSE_LOCATION:
                permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
                break;
            case INT_CAMERA:
                permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
                break;
            case INT_STORAGE:
                permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            default:
                break;
        }
        return permissionStatus;
    }

    public static void showPermissionExplanation(final Context context, final int permission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_app_logo);
        builder.setTitle("Permission needed");
        builder.setCancelable(false);
        switch (permission) {
            case INT_CAMERA:
                builder.setMessage("This App need to access your device camera, Please allow !");
                break;
            case INT_STORAGE:
                builder.setMessage("This App need to access your device storage, Please allow !");
                break;
            case INT_FINE_LOCATION:
                builder.setMessage("This App need to access your device fine location, Please allow !");
                break;
            case INT_COARSE_LOCATION:
                builder.setMessage("This App need to access your device coarse location, Please allow !");
                break;
            case INT_READ_PHONE_STATE:
                builder.setMessage("This App need to access your device phone details, Please allow !");
                break;
        }
        builder.setPositiveButton(context.getString(R.string.allow), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermission(context, permission);
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void requestPermission(final Context context, int permission) {
        switch (permission) {
            case INT_CAMERA:
                ActivityCompat.requestPermissions((Activity) context, new String[]
                        {Manifest.permission.CAMERA}, REQUEST_CAMERA);
                break;
            case INT_STORAGE:
                ActivityCompat.requestPermissions((Activity) context, new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
                break;
            case INT_FINE_LOCATION:
                ActivityCompat.requestPermissions((Activity) context, new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
                break;
            case INT_COARSE_LOCATION:
                ActivityCompat.requestPermissions((Activity) context, new String[]
                        {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_LOCATION);
                break;
            case INT_READ_PHONE_STATE:
                ActivityCompat.requestPermissions((Activity) context, new String[]
                        {Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                break;
            default:
                break;
        }
    }

    public static void redirectAppSettings(Context context) {
        Toast.makeText(context, context.getResources().getString(R.string.please_allow_requested_permissions_in_app_settings), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }
}
