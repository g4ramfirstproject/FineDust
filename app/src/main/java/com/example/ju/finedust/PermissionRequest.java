package com.example.ju.finedust;

import android.Manifest;
import android.content.Context;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class PermissionRequest {

    Context mcontext;

    public PermissionRequest(Context mcontext) {
        this.mcontext = mcontext;
    }

    public void locationAccess(){

        PermissionListener locationListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            }
        };

        TedPermission.with(mcontext)
                .setPermissionListener(locationListener)
                .setRationaleMessage("이 앱을 사용하려면 위치 권한이 필요합니다.")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

    }

}
