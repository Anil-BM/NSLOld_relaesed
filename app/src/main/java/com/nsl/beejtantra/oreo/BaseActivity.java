package com.nsl.beejtantra.oreo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.nsl.beejtantra.commonutils.Common;

/**
 * Created by user on 18-07-2016.
 */
public abstract class BaseActivity extends AppCompatActivity implements PermissionUtil.PermissionAskListener
{


    private static final int CAMERA_REQUEST_CODE = 3;
    final int GPS_REQUEST_CODE=1;
    final int WRITE_REQUEST_CODE=2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("activity",this.getClass().getSimpleName());
        if (!this.getClass().getSimpleName().equalsIgnoreCase("SplashScreen")) {
            PermissionUtil.checkPermission(this, this, Manifest.permission.ACCESS_FINE_LOCATION, this, GPS_REQUEST_CODE);

        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }




    public void checkWriteExternalStoragePermission(Context context,String permission,int request_code) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{permission},
                    request_code);
    }


    @Override
    public void onNeedPermission(String permission,int requestCode) {
        Log.d("onNeedPermission","onNeedPermission..");
        checkWriteExternalStoragePermission(this,permission,requestCode);

    }

    @Override
    public void onPermissionPreviouslyDenied(String permission,int requestCode) {
        Log.d("onNeedPermission","onPermissionPreviouslyDenied..");
        checkWriteExternalStoragePermission(this,permission,requestCode);
    }

    @Override
    public void onPermissionDisabled(String permission,int requestCode) {
        Log.d("onNeedPermission","onPermissionDisabled..");
    }

    @Override
    public void onPermissionGranted(String permission,int requestCode) {
        Log.d("onNeedPermission","onPermissionGranted..");
        if (requestCode==GPS_REQUEST_CODE){
            PermissionUtil.checkPermission(this,this,Manifest.permission.WRITE_EXTERNAL_STORAGE,this,WRITE_REQUEST_CODE);
        }else if (requestCode==WRITE_REQUEST_CODE){
            PermissionUtil.checkPermission(this,this,Manifest.permission.CAMERA,this,CAMERA_REQUEST_CODE);

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Common.Log.i("Permisiion.... "+requestCode+" \n "+ permissions.length+" \n"+grantResults);
        switch (requestCode) {

            case GPS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                PermissionUtil.checkPermission(this,this,Manifest.permission.WRITE_EXTERNAL_STORAGE,this,WRITE_REQUEST_CODE);
                return;

            case WRITE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                PermissionUtil.checkPermission(this,this,Manifest.permission.CAMERA,this,CAMERA_REQUEST_CODE);
                return;


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
