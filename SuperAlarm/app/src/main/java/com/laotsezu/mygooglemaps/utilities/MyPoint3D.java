package com.laotsezu.mygooglemaps.utilities;

/**
 * Created by laotsezu on 14/10/2016.
 */

public class MyPoint3D {
    private float x;
    private float y;
    private float z;
    public MyPoint3D(float x,float y,float z){
        this.z = z;
        this.x = x;
        this.y = y;
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
    public double distanceTo(MyPoint3D _point){
        float tmp1 = (x - _point.getX()) * (x - _point.getX());
        float tmp2 = (y - _point.getY()) * (y - _point.getY());
        float tmp3 = (z - _point.getZ()) * (z - _point.getZ());
        return Math.sqrt(tmp1 + tmp2 + tmp3);
    }
}
