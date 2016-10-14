package com.laotsezu.mygooglemaps;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.laotsezu.mygooglemaps.utilities.MyPermissionManager;

import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, android.location.LocationListener {
    private static String TAG = "MapsActivity";
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private AudioManager mAudioManager;
    private Vibrator mVibrator;
    private MediaPlayer mediaPlayer;

    Location origin_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

/*


* */
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    public void addLocationUpdateListener() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (MyPermissionManager.checkAndRequestPermissions(this
                    , Manifest.permission.ACCESS_COARSE_LOCATION
                    , Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.VIBRATE
            )) {
                if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 0.1f, this);
                else if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,200,0.1f,this);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 0.1f, this);
            else if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,200,0.1f,this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.yeuvoivang);

        addLocationUpdateListener();

        // Add a marker in Sydney and move the camera
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        addLocationUpdateListener();
    }

    @Override
    public void onLocationChanged(Location location) {

        if (origin_location == null) {
            origin_location = location;
        }
        if (origin_location.distanceTo(location) > 500) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.removeUpdates(this);
                if(mediaPlayer.isPlaying())
                    mediaPlayer.stop();
            }
        }
        else{
            Toast.makeText(this, "Not enough, current distance = " + origin_location.distanceTo(location), Toast.LENGTH_SHORT).show();
        }

        LatLng current_position = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(current_position).title("Marker in " + location.getProvider()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current_position));

        mVibrator.vibrate(500);
        mediaPlayer.start();
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
