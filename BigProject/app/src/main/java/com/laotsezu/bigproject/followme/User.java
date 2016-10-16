package com.laotsezu.bigproject.followme;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.facebook.Profile;
import com.laotsezu.bigproject.utilities.BSResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class User extends BaseObservable {
    private static final String TAG = "User";
    private String id;
    private String[] meFollow;
    private String[] followMe;
    interface OnPostLocationListener{
        void onPostLocationCompleted(boolean status,String message);
    }
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
    /*public ? getLocation(){

    }
    public ? getRoadTo(String _otherUserId){

    }*/
    public void putMeFollow(String _otherUserId){

    }
    public void removeMeFollow(String _otherUserId){

    }
    public void disableFollowMe(String _otherUserId){

    }
    static void postLocation(UserLocationInfo userLocationInfo,OnPostLocationListener listener){
        Log.e(TAG,"Start post location with userId = " + userLocationInfo.getId() + ",userLocation = " + userLocationInfo.getLocation());
        PostLocationTask task = new PostLocationTask(listener);
        task.execute(userLocationInfo);
    }
    private static class PostLocationTask extends AsyncTask<UserLocationInfo,Void,BSResponse>{
        private static final String TAG = "PostLocationTask";
        OnPostLocationListener listener;
        private static final String API_URL = "http://kiot.igarden.vn/followme/postlocation";
        PostLocationTask(OnPostLocationListener listener){
            this.listener = listener;
        }
        @Override
        protected BSResponse doInBackground(UserLocationInfo... params) {

            UserLocationInfo info = params[0];
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(API_URL);

            try{
                httpPost.setEntity(new UrlEncodedFormEntity(info.getParams()));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                if(httpResponse.getStatusLine().getStatusCode() == 200){
                    String line = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent())).readLine();
                    JSONObject response = new JSONObject(line);
                    return new BSResponse(response);
                }
                else{
                    return new BSResponse(false,"Response status code ~!= 200");
                }
            }
            catch (Exception e){
                Log.e(TAG,e.getMessage());
                return new BSResponse(false,e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(BSResponse response) {
            super.onPostExecute(response);
            if(response == null){
                listener.onPostLocationCompleted(false,"response from DoinBackground is empty");
            }
            else{
                    if(response.getStatus()){
                        listener.onPostLocationCompleted(true,"no message~");
                    }
                    else{
                        listener.onPostLocationCompleted(false,response.getMessage());
                    }
            }
        }
    }
    static class UserLocationInfo {
        String id;
        String location;

         UserLocationInfo(String id, String location) {
            this.id = id;
            this.location = location;
        }

         List<NameValuePair> getParams() {
            List<NameValuePair> params = new LinkedList<>();
            params.add(new BasicNameValuePair("userId", id));
            params.add(new BasicNameValuePair("userLocation", location));
            return params;
        }

        public String getId() {
            return id;
        }

        public String getLocation() {
            return location;
        }
    }
}
