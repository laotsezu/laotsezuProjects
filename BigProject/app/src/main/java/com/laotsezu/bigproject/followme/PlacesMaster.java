package com.laotsezu.bigproject.followme;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

/**
 * Created by laotsezu on 16/10/2016.
 */

public class PlacesMaster implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    private GoogleApiClient mGoogleApiClient;
    private PlacesMaterCall mCall;
    private Context context;
    interface PlacesMaterCall{
        Context getContext();
        Context getApplicationContext();
        void onConnectedGoogleApi();
        void onConnectGoogleApiFailed(String message);
    }
    public PlacesMaster(PlacesMaterCall mCall){
        this.mCall = mCall;
        this.context = mCall.getContext();

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }
    void connectGoogleApi() {
        mGoogleApiClient.connect();
    }
    void disconnectGoogleApi(){
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mCall.onConnectedGoogleApi();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mCall.onConnectGoogleApiFailed(connectionResult.getErrorMessage());
    }


}
