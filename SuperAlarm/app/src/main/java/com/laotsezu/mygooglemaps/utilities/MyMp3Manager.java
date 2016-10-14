package com.laotsezu.mygooglemaps.utilities;

import android.content.Context;
import android.media.MediaPlayer;

import com.laotsezu.mygooglemaps.R;

/**
 * Created by Laotsezu on 10/12/2016.
 */

public class MyMp3Manager {
    private MediaPlayer mediaPlayer;
    private boolean playing  = false;
    public MyMp3Manager(Context context){
        mediaPlayer = MediaPlayer.create(context, R.raw.bao_thuc);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(playing)
                    mp.start();
            }
        });
    }
    public void startAlarm(){
        playing = true;
        mediaPlayer.start();

    }
    public void startingAlarm(){
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
    }
    public void stopAlarm(){
        playing = false;
        mediaPlayer.stop();

    }
    public long getMaxPlayTime(){
        return mediaPlayer.getDuration();
    }

}
