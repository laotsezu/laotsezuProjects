package com.laotsezu.bigproject.followme;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
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
import com.laotsezu.bigproject.utilities.MySharePreferencesManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by laotsezu on 16/10/2016.
 */

public class LocationMaster implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, User.OnPostLocationListener, LocationListener {
    private static final String TAG = "LocationMaster";
    private static final long NAM_PHUT = 1000 * 60 * 5;
    private static final long HAI_PHUT = 1000 * 60 * 2;
    private static final float MUOI_MET = 10;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Context context;
    private LocationMasterCall mCall;
    private Location mLocation;

    @Override
    public void onPostLocationCompleted(boolean status, String message) {
        Toast.makeText(context, "Post location complete with status = " + status + ", message = " + message, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Post location complete with status = " + status + ", message = " + message);
    }

    public Location getCurrentLocation() throws Exception{
        if(mLocation == null)
            throw new Exception();
        else{
            return mLocation;
        }
    }

    public interface LocationMasterCall {
        Context getContext();
        Context getApplicationContext();
        void onConnectedGoogleApi();
        void onConnectGoogleApiFailed(String message);
    }

    public LocationMaster(LocationMasterCall mCall) {
        this.mCall = mCall;
        this.context = mCall.getContext();
        this.mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        this.context = mGoogleApiClient.getContext();
        this.mLocationRequest = new LocationRequest();
        settingLocationRequest();
    }

    void connectGoogleApi() {
        mGoogleApiClient.connect();
    }
    void disconnectGoogleApi(){
        mGoogleApiClient.disconnect();
    }

    private void settingLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(NAM_PHUT);
        mLocationRequest.setFastestInterval(NAM_PHUT);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private boolean isLocationAvailability() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        return locationAvailability != null && locationAvailability.isLocationAvailable();
    }

    private void postLocation(Location location) {
        if (ConnectUtils.hasNetworkConnect(context)) {
            if(location != null){
                this.mLocation = location;
                //post my location to server
                String userLocation = location.getLatitude() + "," + location.getLongitude();
                String userId = MySharePreferencesManager.getUserId(mCall.getApplicationContext(),null);

                if(userId != null){
                    User.postLocation(new User.UserLocationInfo(userId,userLocation),this);
                }
            }
            else{
                onPostLocationCompleted(false,"Post location failed, location is empty");
            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mCall.onConnectedGoogleApi();
    }
    void postLastLocation(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {return;}
        if (isLocationAvailability()) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            postLocation(location);
        } else {
            Log.e(TAG, "Location unavailability~");
            Toast.makeText(context, "Please, Hãy bật GPS~", Toast.LENGTH_SHORT).show();
        }
    }
    void startRequestAndPostLocationUpdates(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {return;}
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }
    void stopRequestAndPostLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
    }
    @Override
    public void onLocationChanged(Location location) {
        if(isLocationAvailability())
            postLocation(location);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mCall.onConnectGoogleApiFailed(connectionResult.getErrorMessage());
    }
}
