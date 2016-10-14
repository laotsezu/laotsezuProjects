package com.laotsezu.mygooglemaps.utilities;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;


/**
 * Created by Laotsezu on 10/12/2016.
 */

public class MyLocationTracker implements LocationListener {
    private LocationManager mLocationManager;
    private static long LOCATION_UPDATE_MINTIME = 0;
    private static float LOCATION_UPDATE_MINDISTANCE = 0;
    private Location origin_location,current_location;
    private static String TAG = "MyLocationTracked: ";

    private Context context;

    private MyLocationTracker(Context context) {
        this.context = context;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public static MyLocationTracker getInstance(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        return new MyLocationTracker(context);
    }

    private boolean isGPSProvidable() {
        boolean result = mLocationManager.isProviderEnabled(getGPSProvider());
        Log.e(TAG,"Is GPS providable = " + result);
        return result;
    }

    private boolean isNetworkProviable() {
        boolean result = mLocationManager.isProviderEnabled(getNetworkProvider());
        Log.e(TAG,"Is Network providable = " + result);
        return result;
    }

    private String getGPSProvider() {
        return LocationManager.GPS_PROVIDER;
    }

    private String getNetworkProvider() {
        return LocationManager.NETWORK_PROVIDER;
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void startTrackLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        if (isGPSProvidable()) {
            mLocationManager.requestLocationUpdates(getGPSProvider(), LOCATION_UPDATE_MINTIME, LOCATION_UPDATE_MINDISTANCE, this);
            origin_location = mLocationManager.getLastKnownLocation(getGPSProvider());
            current_location = origin_location;
        }
        if (isNetworkProviable()) {
            mLocationManager.requestLocationUpdates(getNetworkProvider(), LOCATION_UPDATE_MINTIME, LOCATION_UPDATE_MINDISTANCE, this);
            origin_location = mLocationManager.getLastKnownLocation(getNetworkProvider());
            current_location = origin_location;
        }
    }

    public void stopTrackLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {}
        mLocationManager.removeUpdates(this);
    }
    public Location getCurrentLocation(){
        return current_location;
    }
    public float getDistanceFromOriginLocation(){
        if(origin_location != null && current_location != null)
            return calculateDistanceBetween2Location(origin_location, current_location);
        if(origin_location == null){
            Log.e(TAG,"Origin Location =  Null");
        }
        if(current_location == null){
            Log.e(TAG,"Current Location = Null");
        }
            return 0;
    }
    public static float calculateDistanceBetween2Location(Location l1,Location l2){
        return l2.distanceTo(l1);
    }
    public static float calculateDistanceBetween2Point(LatLng point1,LatLng point2){
        float results[] = {};
        Location.distanceBetween(point1.latitude,point1.longitude,point2.latitude,point2.longitude,results);
        return results[0];
    }
    @Override
    public void onLocationChanged(Location location) {
        if(origin_location == null)
            origin_location = location;
        this.current_location = location;
        Log.e(TAG,"Current Location Change!");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
