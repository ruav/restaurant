package com.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.restaurant.entity.Image;

import java.io.Serializable;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PhotoDto implements Serializable, Image {

    private long id;
    private String url;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PhotoDto{" +
                "id=" + id +
                ", url='" + url + '\'' +
                '}';
    }
}
