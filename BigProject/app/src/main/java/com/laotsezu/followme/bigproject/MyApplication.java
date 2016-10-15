package com.laotsezu.followme.bigproject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.FacebookActivity;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.laotsezu.bigproject.followme.PostLocationReceiver;

/**
 * Created by laotsezu on 15/10/2016.
 */

public class MyApplication extends Application{
    public static final String SHARE_PREFERENCES_CODE = "laotsezuProjects";
    private static final String TAG = "MyApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


    }
    public static void schedulePostLocation(Context context, Profile profile){
        if(profile != null) {
            SharedPreferences mSharePreferences = context.getSharedPreferences(SHARE_PREFERENCES_CODE, MODE_PRIVATE);
            if (!mSharePreferences.getBoolean("isScheduledPostLocation", false)) {
                PostLocationReceiver.setupPostSchedule(context.getApplicationContext(), profile.getId());
                mSharePreferences.edit().putBoolean("isSchedulePostLocation", true).apply();
            }
        }
        else{
            Log.e(TAG,"Profile == Null");
        }
    }
}
