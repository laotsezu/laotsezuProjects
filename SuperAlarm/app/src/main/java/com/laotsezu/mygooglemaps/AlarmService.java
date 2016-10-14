package com.laotsezu.mygooglemaps;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.laotsezu.mygooglemaps.utilities.MyDistanceTracker;
import com.laotsezu.mygooglemaps.utilities.MyLocationTracker;
import com.laotsezu.mygooglemaps.utilities.MyMp3Manager;

public class AlarmService extends Service {
    MyMp3Manager mp3Manager;
    AudioManager mAudioManager;
    MyLocationTracker mLocationTracker;
    MyDistanceTracker mDistanceTracker;
    long origin_milisecond;
    long time_alarm;
    float distance_alarm = 1200;
    Toast toast;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void init(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {}
        mp3Manager = new MyMp3Manager(this);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mLocationTracker = MyLocationTracker.getInstance(this);
        mDistanceTracker = new MyDistanceTracker(this);
        time_alarm = mp3Manager.getMaxPlayTime() * 2;
        origin_milisecond = System.currentTimeMillis();
        toast = new Toast(this);
    }
    private void start(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {}
        mp3Manager.startAlarm();
        mDistanceTracker.startTrackDistance();
    }
    private void end(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long time_alarmed = System.currentTimeMillis() - origin_milisecond;
                float current_distance = mLocationTracker.getDistanceFromOriginLocation();

                if(time_alarmed > time_alarm || (current_distance > distance_alarm)) {
                    mp3Manager.stopAlarm();
                    mDistanceTracker.stopTrackDistance();
                    stopSelf();
                }
                else{
                    toast.cancel();
                    try {
                        toast = Toast.makeText(AlarmService.this, "Remain: " + ((time_alarm - time_alarmed) / 1000) + "s or " + (distance_alarm - mDistanceTracker.getDistanceFromOrigin()), Toast.LENGTH_SHORT);
                        toast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

                    if(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) < maxVolume){
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,maxVolume,AudioManager.FLAG_PLAY_SOUND);
                    }

                    handler.postDelayed(this,1000);
                }
            }
        });
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        init();
        start();
        end();

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
