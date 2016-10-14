package com.laotsezu.mygooglemaps;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.laotsezu.mygooglemaps.utilities.MyMp3Manager;

public class AlarmActivity extends AppCompatActivity {
    private static String TAG= "AlarmActivity: ";
    private MyMp3Manager mp3Manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_alarm);

        init();
    }
    private void init(){
        mp3Manager = new MyMp3Manager(this);
        Log.e(TAG,"Start Alarm");
        mp3Manager.startAlarm();

        findViewById(R.id.alarm_button).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            long current_mili = 0;
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    current_mili = System.currentTimeMillis();
                }
                else{
                    long next_mili = System.currentTimeMillis();
                    if(next_mili - current_mili > 1000 * 60 * 60)
                        mp3Manager.stopAlarm();
                }
            }
        });
    }
}
