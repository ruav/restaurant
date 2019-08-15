package com.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;

public class StatusDto {

    @ApiModelProperty
    private String datetime;
    @ApiModelProperty
    private Integer status;
    @ApiModelProperty
    private Integer hostes;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getHostes() {
        return hostes;
    }

    public void setHostes(Integer hostes) {
        this.hostes = hostes;
    }

    @Override
    public String toString() {
        return "StatusDto{" +
                "datetime='" + datetime + '\'' +
                ", status=" + status +
                ", hostes=" + hostes +
                '}';
    }
}
