package com.laotsezu.followme.bigproject.followme;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class User extends BaseObservable {
    private String id;
    private String[] meFollow;
    private String[] followMe;
    //private Location mLocation;
    @Bindable
    public String getId() {
        return id;
    }
    @Bindable
    public String[] getMeFollow() {
        return meFollow;
    }
    @Bindable
    public String[] getFollowMe() {
        return followMe;
    }
    public ? getLocation(){

    }
    public ? getRoadTo(String _otherUserId){

    }
    public void putMeFollow(String _otherUserId){

    }
    public void removeMeFollow(String _otherUserId){

    }
    public void disableFollowMe(String _otherUserId){

    }
}
