package com.laotsezu.followme.bigproject.followme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by laotsezu on 15/10/2016.
 */

public class PostLocationReceiver extends BroadcastReceiver{
    public static final int POST_SCHEDULE_CODE = 51995;
    private static final long NAM_PHUT = 60 * 1000 * 5;
    public static void setupPostSchedule(Context context,String userId){
        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context,PostLocationReceiver.class);
        intent.putExtra("userId",userId);
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(context,POST_SCHEDULE_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        mAlarmManager.cancel(mPendingIntent);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),NAM_PHUT,mPendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent old_intent) {
        Intent intent = new Intent(context,PostLocationService.class);
        intent.putExtra("userId",old_intent.getExtras().getString("userId"));
        context.startService(intent);
    }
}
