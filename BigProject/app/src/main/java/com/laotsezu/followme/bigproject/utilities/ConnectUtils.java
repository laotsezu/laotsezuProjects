package com.laotsezu.followme.bigproject.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by laotsezu on 15/10/2016.
 */

public class ConnectUtils {
    public static boolean hasNetworkConnect(Context context){
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return mNetworkInfo != null && mNetworkInfo.isAvailable() && mNetworkInfo.isConnected();
    }
}
