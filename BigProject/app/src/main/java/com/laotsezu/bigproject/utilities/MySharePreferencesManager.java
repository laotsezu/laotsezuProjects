package com.laotsezu.bigproject.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.Profile;

/**
 * Created by laotsezu on 15/10/2016.
 */

public class MySharePreferencesManager {
    static final String TAG = "MySharePreManager";
    public static final String SHARE_PREFERENCES_CODE = "laotsezuProjects";
    private Context context;
    private SharedPreferences mSharePreferences;
    private MySharePreferencesManager(Context context){

        this.context = context;
        mSharePreferences = context.getSharedPreferences(SHARE_PREFERENCES_CODE,Context.MODE_PRIVATE);
    }
    public static void onLoginSuccessful(Context context,Profile profile){
        Log.e(TAG,"onLoginSuccessful");
        MySharePreferencesManager manager = new MySharePreferencesManager(context);
        manager.editStringValue("userId",profile.getId());
    }
    public static void onScheduledPostLocation(Context context){
        Log.e(TAG,"onScheduledPostLocation");
        MySharePreferencesManager manager = new MySharePreferencesManager(context);
        manager.editBooleanValue("isScheduledPostLocation",true);
    }
    public static boolean isScheduledPostLocation(Context context,boolean defaultValue){
        return new MySharePreferencesManager(context).mSharePreferences.getBoolean("isScheduledPostLocation",defaultValue);
    }
    public static String getUserId(Context context,String defaultValue){
        return new MySharePreferencesManager(context).mSharePreferences.getString("userId",defaultValue);
    }
    private void editBooleanValue(String index,boolean value){
        mSharePreferences.edit().putBoolean(index,value).apply();
    }
    private void editStringValue(String index,String value){
        mSharePreferences.edit().putString(index,value).apply();
    }
}
