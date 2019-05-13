package com.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.Serializable;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PhotoDto implements Serializable {

    private String url;

    public PhotoDto( String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PhotoDto{" +
                "url='" + url + '\'' +
                '}';
    }
}
