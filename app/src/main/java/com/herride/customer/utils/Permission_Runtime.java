package com.herride.customer.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permission_Runtime {

    public static Integer PERMISSION_ALL = 1;

    public static final int PERMISSION_CODE = 101;

    static String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
    };





    //public static void askForPermission(Integer requestCode, Activity context,Context mcontext)
    public static void askForPermission(Integer requestCode, Context mcontext) {
        if (ContextCompat.checkSelfPermission(mcontext, String.valueOf(PERMISSIONS)) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ContextCompat.checkSelfPermission(mcontext, String.valueOf(PERMISSIONS)) != PackageManager.PERMISSION_GRANTED) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions((Activity) mcontext, PERMISSIONS, requestCode);

            } else {

                ActivityCompat.requestPermissions((Activity) mcontext, PERMISSIONS, requestCode);
            }
        } else {
            //Toast.makeText(getActivity(), "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }




}
