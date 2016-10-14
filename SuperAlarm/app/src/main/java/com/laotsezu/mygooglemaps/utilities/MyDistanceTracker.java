package com.laotsezu.mygooglemaps.utilities;

import android.content.Context;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by laotsezu on 14/10/2016.
 */

public class MyDistanceTracker implements SensorListener {
    private static final String TAG = "MyDistanceTracker";
    private Context context;
    private SensorManager mSensorManager;
    private long lastTime;
    private MyAccelerometer currentAccelerometer;
    private double distance;
    public MyDistanceTracker(Context context){
        this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }
    public void startTrackDistance(){
        mSensorManager.registerListener(this,SensorManager.SENSOR_ACCELEROMETER,SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void stopTrackDistance(){
        mSensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(int sensor, float[] values) {
        if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastTime) > 100) {
                long diffTime = (curTime - lastTime);
                lastTime = curTime;

                if(currentAccelerometer == null){
                    currentAccelerometer = new MyAccelerometer(values[SensorManager.DATA_X],values[SensorManager.DATA_Y],values[SensorManager.DATA_Z]);
                }
                else{
                    currentAccelerometer.updateValue(values[SensorManager.DATA_X],values[SensorManager.DATA_Y],values[SensorManager.DATA_Z]);
                }

                distance += currentAccelerometer.getLastDistance();


                try {
                    Log.e(TAG,"Current Distance = " + getDistanceFromOrigin());
                } catch (Exception e) {
                    Log.e(TAG,e.getMessage());
                }
            }
        }
    }
    public double getDistanceFromOrigin() throws Exception{
        return distance;
    }
    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {

    }
}
