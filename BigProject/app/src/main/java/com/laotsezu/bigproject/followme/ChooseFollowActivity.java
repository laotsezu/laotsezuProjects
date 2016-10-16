package com.laotsezu.bigproject.followme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.laotsezu.bigproject.R;
import com.laotsezu.bigproject.databinding.ViewChooseFollowBinding;
import com.laotsezu.bigproject.utilities.MyPermissionManager;
import com.laotsezu.bigproject.utilities.MySharePreferencesManager;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChooseFollowActivity extends AppCompatActivity{
    final static String TAG = "ChooseFollowAct";
    ViewChooseFollowBinding chooseFollowBinding;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chooseFollowBinding = DataBindingUtil.setContentView(this, R.layout.view_choose_follow);

        userId = MySharePreferencesManager.getUserId(getApplicationContext(), null);


        Intent intent = new Intent(this,PostLocationService.class);
        startService(intent);

        Log.e(TAG, "here is choose follow activity!");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
