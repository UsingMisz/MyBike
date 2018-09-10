package com.example.administrator.mybike;

import java.io.Serializable;

public class MapLocationInfo implements Serializable {
    private static final long serialVersionUID = 12345678L;

    private double latitude;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    private float radius;

    private double longitude;



    private String coorType;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public String getCoorType() {
        return coorType;
    }

    public void setCoorType(String coorType) {
        this.coorType = coorType;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    private int errorCode;



}
