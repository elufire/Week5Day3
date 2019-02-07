package com.example.week5day3.managersandmodels;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class PermissionsManager {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2018;
    IPermissionManager iPermissionManager;
    Context context;

    public PermissionsManager(IPermissionManager iPermissionManager, Context context) {
        this.iPermissionManager = iPermissionManager;
        this.context = context;
    }

    public void checkPermission(){
        // Here, (Activity)context is the current activity
        if (ContextCompat.checkSelfPermission((Activity)context,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                    requestPermission();
            }
        } else {
           iPermissionManager.onPerissionResult(true);
        }
    }

    public void requestPermission(){
        ActivityCompat.requestPermissions((Activity)context,
                new String[] {Manifest.permission.READ_CONTACTS},
        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }

    public  void checkResult(int requestCode,
                             String permissions[], int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:{
                if(grantResults.length >0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d("TAG", "onRequestPermission");

                    iPermissionManager.onPerissionResult(true);
                }else {
                    iPermissionManager.onPerissionResult(true);
                    Log.d("TAG", "oNrequestPermissionDenied");
                }
                return;
            }
        }
    }

    public interface IPermissionManager{
        void onPerissionResult(boolean isGranted);
    }

}
