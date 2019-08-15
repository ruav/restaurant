package com.restaurant.dto;

import com.restaurant.entity.Data;
import io.swagger.annotations.ApiModelProperty;

public class TagDto implements Data {

    @ApiModelProperty
    private long id;
    @ApiModelProperty
    private String name;
    @ApiModelProperty
    private long lastChange;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastChange() {
        return lastChange;
    }

    public void setLastChange(long lastChange) {
        this.lastChange = lastChange;
    }

    @Override
    public String toString() {
        return "Hostes{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastChange=" + lastChange +
                '}';
    }
}
