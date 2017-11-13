package com.dreampany.framework.ui.activity;

import android.Manifest;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.dreampany.framework.data.manager.PermissionManager;
import com.dreampany.framework.data.util.AndroidUtil;

/**
 * Created by nuc on 12/11/2016.
 */

public class PermissionActivity extends AppCompatActivity {

    protected static final int REQUEST_LOCATION = 101;

    private static final String PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    protected void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= 21) {
            if (!hasLocationPermission()) {
                AndroidUtil.log("Permission", "Don\'t have permission for location - asking");
                ActivityCompat.requestPermissions(this, new String[]{PERMISSION_LOCATION}, REQUEST_LOCATION);
            } /*else {
                AndroidUtil.log("MeshPermission", "wifi scans allowed");
                this.finish();
            }*/
        }
    }

    protected boolean hasLocationPermission() {
        return PermissionManager.isPermissionGranted(this, PERMISSION_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

/*        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    requestLocationPermission();
                }

                break;
        }*/

    }
}
