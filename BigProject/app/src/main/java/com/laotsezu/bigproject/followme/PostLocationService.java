package com.laotsezu.bigproject.followme;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.laotsezu.bigproject.utilities.ConnectUtils;
import com.laotsezu.bigproject.utilities.IntentUtils;
import com.laotsezu.bigproject.utilities.MySharePreferencesManager;

/**
 * Created by laotsezu on 15/10/2016.
 */

public class PostLocationService extends Service implements LocationMaster.LocationMasterCall{
    private final static String TAG = "PostLocationSer";
    String userId;
    String latLng;
    LocationMaster mLocationMaster;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "On start Command");

        if(mLocationMaster == null){
            mLocationMaster = new LocationMaster(this);
        }

        mLocationMaster.connectGoogleApi();

        return START_STICKY;
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onConnectedGoogleApi() {
        Log.e(TAG,"Google api connected!");
        mLocationMaster.postLastLocation();
        mLocationMaster.startRequestAndPostLocationUpdates();
    }

    @Override
    public void onConnectGoogleApiFailed(String message) {
        Log.e(TAG,"Connect google api failed!");
        Toast.makeText(this, "Connect gogole api failed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"Post location service destroy!");
        Toast.makeText(this, "Post Location Service destroy!", Toast.LENGTH_SHORT).show();
        mLocationMaster.stopRequestAndPostLocationUpdates();
        mLocationMaster.disconnectGoogleApi();
    }
}
