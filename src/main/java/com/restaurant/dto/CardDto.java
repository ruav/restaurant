package com.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;

public class CardDto {

    @ApiModelProperty
    private long id;
    @ApiModelProperty(name = "hall_id")
    private long hallId;
    @ApiModelProperty
    private String map;
    @ApiModelProperty
    private String relevantFrom;
    @ApiModelProperty
    private long lastChange;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHallId() {
        return hallId;
    }

    public void setHallId(long hallId) {
        this.hallId = hallId;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getRelevantFrom() {
        return relevantFrom;
    }

    public void setRelevantFrom(String relevantFrom) {
        this.relevantFrom = relevantFrom;
    }

    public long getLastChange() {
        return lastChange;
    }

    public void setLastChange(long lastChange) {
        this.lastChange = lastChange;
    }

    @Override
    public String toString() {
        return "CardDto{" +
                "id=" + id +
                ", hallId='" + hallId + '\'' +
                ", map='" + map + '\'' +
                ", relevantFrom='" + relevantFrom + '\'' +
                ", lastChange=" + lastChange +
                '}';
    }
}
