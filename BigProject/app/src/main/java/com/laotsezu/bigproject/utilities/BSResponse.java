package com.laotsezu.bigproject.utilities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by laotsezu on 15/10/2016.
 */

public class BSResponse {
    private String message;
    private boolean status;
    public BSResponse(JSONObject input){
        try {
            this.status = input.getBoolean("status");
            if(!status) {
                input.getString("message");
            }
        } catch (JSONException e) {
            this.status = false;
            this.message = e.getMessage();
        }
    }
    public BSResponse(boolean status,String message){
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public boolean getStatus() {
        return status;
    }
}
