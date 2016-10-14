package com.laotsezu.mygooglemaps.utilities;

/**
 * Created by laotsezu on 14/10/2016.
 */

public class MyAccelerometer {
    private float x;
    private float y;
    private float z;

    private float lastX,lastY,lastZ;

    public MyAccelerometer(float x, float y, float z){
        this.z = z;
        this.x = x;
        this.y = y;

        this.lastX = this.x;
        this.lastY = this.y;
        this.lastZ = this.z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
    public void updateValue(float x,float y,float z){
        this.lastX = this.x;
        this.lastY = this.y;
        this.lastZ = this.z;

        this.x = x;
        this.y = y;
        this.z = z;
    }
    public double getLastDistance(){
        return Math.abs(x + y + z - lastX - lastY - lastZ);
    }
}
