package com.laotsezu.bigproject;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by laotsezu on 15/10/2016.
 */

public class MyApplication extends Application{

    private static final String TAG = "MyApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


    }

}
