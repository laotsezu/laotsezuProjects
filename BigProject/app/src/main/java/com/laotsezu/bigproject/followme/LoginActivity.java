package com.laotsezu.bigproject.followme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.laotsezu.bigproject.MyApplication;
import com.laotsezu.bigproject.R;
import com.laotsezu.bigproject.databinding.ViewLoginBinding;
import com.laotsezu.bigproject.utilities.MyPermissionManager;
import com.laotsezu.bigproject.utilities.MySharePreferencesManager;

import java.util.Arrays;
import java.util.Collections;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Login Activity";
    ViewLoginBinding loginBinding;
    LoginManager mLoginManager;
    CallbackManager mCallbackManager;
    ProfileTracker mProfileTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(MyPermissionManager.checkAndRequestPermissions(
                    this
                    ,Manifest.permission.ACCESS_COARSE_LOCATION
                    ,Manifest.permission.ACCESS_FINE_LOCATION

            )){
                init();
            }
        }
        else{
            init();
        }
    }
    public void init(){
        loginBinding = DataBindingUtil.setContentView(this,R.layout.view_login);
        mLoginManager = LoginManager.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        mLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG,"Login Successful!");
                Profile profile = Profile.getCurrentProfile();
                MySharePreferencesManager.onLoginSuccessful(getApplicationContext(),profile);
                toChooseFollowActivity();
            }
            @Override
            public void onCancel() {

            }
            @Override
            public void onError(FacebookException error) {
                Log.e(TAG,"Login failed, " + error.getMessage());
            }
        });
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Profile.setCurrentProfile(currentProfile);
            }
        };
        mProfileTracker.startTracking();
        mLoginManager.logInWithReadPermissions(this, Collections.singletonList("email"));
    }
    public void toChooseFollowActivity(){
        Intent intent = new Intent(this,ChooseFollowActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mCallbackManager != null){
            mCallbackManager.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean grantedAll = true;
        for (int grantResult:grantResults) {
            if(grantResult != PackageManager.PERMISSION_GRANTED)
                grantedAll = false;
        }
        if(grantedAll){
            init();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mProfileTracker != null){
            mProfileTracker.stopTracking();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
