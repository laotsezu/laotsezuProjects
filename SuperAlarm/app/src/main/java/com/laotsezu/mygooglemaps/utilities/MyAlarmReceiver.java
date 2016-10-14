package com.laotsezu.mygooglemaps.utilities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.laotsezu.mygooglemaps.AlarmActivity;
import com.laotsezu.mygooglemaps.AlarmService;

import java.util.Calendar;

/**
 * Created by Laotsezu on 10/12/2016.
 */

public class MyAlarmReceiver extends BroadcastReceiver {
    private static String TAG = "MyAlarmReceiver: ";
    @Override
    public void onReceive(Context context, Intent data) {
        Log.e(TAG,"On receive!");
        Intent intent = new Intent(context, AlarmService.class);
        context.startService(intent);
    }
}
