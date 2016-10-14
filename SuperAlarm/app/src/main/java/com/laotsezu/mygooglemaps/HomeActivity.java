package com.laotsezu.mygooglemaps;

import android.*;
import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import com.laotsezu.mygooglemaps.utilities.MyAlarmReceiver;
import com.laotsezu.mygooglemaps.utilities.MyCalendarManager;
import com.laotsezu.mygooglemaps.utilities.MyMp3Manager;
import com.laotsezu.mygooglemaps.utilities.MyPermissionManager;

public class HomeActivity extends AppCompatActivity {
    private static String TAG = "Home Activity: ";
    TimePicker timePicker;
    MyCalendarManager calendarManager;
    PendingIntent alarmIntent;
    final static long repeat_time_milisec = AlarmManager.INTERVAL_DAY;
    final static int repeat_time_day = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_home);
        init();
    }
    private void init(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MyPermissionManager.checkAndRequestPermissions(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                    ,Manifest.permission.ACCESS_FINE_LOCATION
            );
        }

        timePicker = (TimePicker) findViewById(R.id.time_picker_view);
        calendarManager = new MyCalendarManager();

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendarManager.setHour(hourOfDay);
                calendarManager.setMinute(minute);

                Log.e(TAG,"Time Change" + ", Hour of day = " + hourOfDay + ", minute = " + minute);
            }
        });

        findViewById(R.id.time_picker_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });
    }
    public void setAlarm(){
        Log.e(TAG,"Set Alarm!");

        Intent intent = new Intent(this,MyAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this,1001,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if(System.currentTimeMillis() > calendarManager.getTimeInMillis()){
            calendarManager.addDayOfMonth(repeat_time_day);
        }
        alarmManager.setInexactRepeating(AlarmManager.RTC,calendarManager.getCalendar().getTimeInMillis(),repeat_time_milisec,alarmIntent);

        findViewById(R.id.time_picker_button).setVisibility(View.GONE);
        finishAffinity();
    }
    public void removeAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(alarmIntent);
    }
}
