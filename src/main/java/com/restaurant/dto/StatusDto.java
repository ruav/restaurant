package com.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;

public class StatusDto {

    @ApiModelProperty
    private long lastchange;
    @ApiModelProperty
    private Integer status;
    @ApiModelProperty
    private Long hostess;

    public long getDatetime() {
        return lastchange;
    }

    public void setDatetime(long datetime) {
        this.lastchange = datetime;
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
                "datetime='" + lastchange + '\'' +
                ", status=" + status +
                ", hostess=" + hostess +
                '}';
    }
}
