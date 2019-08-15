package com.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;

public class HallDto {

    @ApiModelProperty
    private long id;
    @ApiModelProperty
    private String name;
    @ApiModelProperty
    private boolean active;
    @ApiModelProperty
    private boolean online;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public long getLastChange() {
        return lastChange;
    }

    public void setLastChange(long lastChange) {
        this.lastChange = lastChange;
    }

    @Override
    public String toString() {
        return "HallDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", online=" + online +
                ", lastChange=" + lastChange +
                '}';
    }
}
