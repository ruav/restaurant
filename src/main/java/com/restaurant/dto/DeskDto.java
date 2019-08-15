package com.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;

public class DeskDto {

    @ApiModelProperty
    private long id;
    @ApiModelProperty(name = "hall_id")
    private long hallId;
    @ApiModelProperty
    private Integer number;
    @ApiModelProperty
    private Integer capacity;
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public long getLastChange() {
        return lastChange;
    }

    public void setLastChange(long lastChange) {
        this.lastChange = lastChange;
    }

    @Override
    public String toString() {
        return "DeskDto{" +
                "id=" + id +
                ", hallId='" + hallId + '\'' +
                ", number=" + number +
                ", capacity=" + capacity +
                ", lastChange=" + lastChange +
                '}';
    }

}
