package com.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;

public class StatusDto {

    @ApiModelProperty
    private String datetime;
    @ApiModelProperty
    private Integer status;
    @ApiModelProperty
    private Long hostess;

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

    public Long getHostess() {
        return hostess;
    }

    public void setHostess(Long hostess) {
        this.hostess = hostess;
    }

    @Override
    public String toString() {
        return "StatusDto{" +
                "datetime='" + datetime + '\'' +
                ", status=" + status +
                ", hostess=" + hostess +
                '}';
    }
}
