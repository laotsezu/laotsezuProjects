package com.laotsezu.bigproject.followme;

import android.content.Intent;
import android.databinding.DataBindingUtil;
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
        loginBinding = DataBindingUtil.setContentView(this,R.layout.view_login);

        mLoginManager = LoginManager.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        mLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG,"Login Successful!");
                MyApplication.schedulePostLocation(getApplicationContext(),Profile.getCurrentProfile());

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProfileTracker.stopTracking();
    }
}
