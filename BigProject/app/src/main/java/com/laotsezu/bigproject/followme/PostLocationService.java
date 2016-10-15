package com.laotsezu.bigproject.followme;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by laotsezu on 15/10/2016.
 */

public class PostLocationService extends Service{
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException();
    }
    public static void setPostSchedule(){

    }
}
