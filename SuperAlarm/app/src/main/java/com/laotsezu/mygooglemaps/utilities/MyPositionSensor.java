package com.laotsezu.mygooglemaps.utilities;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by laotsezu on 14/10/2016.
 */

public class MyPositionSensor implements SensorListener {
    private static final String TAG = "MyPositionSensor";
    private Context context;
    private SensorManager mSensorManager;
    private long lastTime;
    MyPoint3D lastPoint,currentPoint;
    public MyPositionSensor(Context context){
        this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this,SensorManager.SENSOR_ACCELEROMETER,SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(int sensor, float[] values) {
        if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastTime) > 100) {
                long diffTime = (curTime - lastTime);
                lastTime = curTime;

                currentPoint = new MyPoint3D(values[SensorManager.DATA_X],values[SensorManager.DATA_Y],values[SensorManager.DATA_Z]);
                if(lastPoint == null)
                    lastPoint = currentPoint;
                try {
                    Log.e(TAG,"Current Distance = " + getDistanceFromOrigin());
                } catch (Exception e) {
                    Log.e(TAG,e.getMessage());
                }
            }
        }
    }
    public double getDistanceFromOrigin() throws Exception{
        if(lastPoint != null && currentPoint != null){
            return lastPoint.distanceTo(currentPoint);
        }
        throw new Exception();
    }
    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {

    }
}
