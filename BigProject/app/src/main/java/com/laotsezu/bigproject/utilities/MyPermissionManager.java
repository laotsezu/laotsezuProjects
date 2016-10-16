package com.laotsezu.bigproject.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Laotsezu on 10/12/2016.
 */

public class MyPermissionManager {
    public static int PERMISSION_REQUEST_CODE = 51211;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean checkAndRequestPermissions(Activity activity, String...permissions){
        List<String> list_permissed_yet = new LinkedList<>();
        for (String permission:permissions) {
            if(ActivityCompat.checkSelfPermission(activity,permission) != PackageManager.PERMISSION_GRANTED){
                list_permissed_yet.add(permission);
            }
        }

        if(list_permissed_yet.size() > 0) {
            String[] list_request = new String[list_permissed_yet.size()];
            for(int i = 0; i < list_permissed_yet.size() ; i++){
                list_request[i] = list_permissed_yet.get(i);
            }
            activity.requestPermissions(list_request, PERMISSION_REQUEST_CODE);
            return false;
        }
        else
            return true;
    }
}
