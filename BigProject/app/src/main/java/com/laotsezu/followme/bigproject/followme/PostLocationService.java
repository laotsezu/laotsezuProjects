package com.laotsezu.followme.bigproject.followme;

import android.app.IntentService;
import android.content.Intent;

import com.laotsezu.bigproject.utilities.ConnectUtils;
import com.laotsezu.bigproject.utilities.IntentUtils;

/**
 * Created by laotsezu on 15/10/2016.
 */

public class PostLocationService extends IntentService {
    public PostLocationService(){
        super("PostLocationService");
    }
    public PostLocationService(String name) {
        super(name);
    }
    protected void onHandleIntent(Intent intent) {
        if(!IntentUtils.isEmpty(intent)) {
            String userId = intent.getExtras().getString("userId",null);
            if(userId != null){
                postLocation(userId);
            }
        }
    }
    public void postLocation(String userId){
        if(ConnectUtils.hasNetworkConnect(this)){
            //post my location to server
        }
    }

}
