package com.restaurant.dto;

import java.io.Serializable;

public class RestaurantDto implements Serializable {

    private long id;
    private float latitude;
    private float longtitude;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
    }
}
