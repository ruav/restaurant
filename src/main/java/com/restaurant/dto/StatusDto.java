package com.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;

public class StatusDto {

    @ApiModelProperty
    private long lastchange;
    @ApiModelProperty
    private Integer status;
    @ApiModelProperty
    private Long hostess;
    @ApiModelProperty
    private Long id;

    public long getLastchange() {
        return lastchange;
    }

    public void setLastchange(long lastchange) {
        this.lastchange = lastchange;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "StatusDto{" +
                "lastchange=" + lastchange +
                ", status=" + status +
                ", hostess=" + hostess +
                ", id=" + id +
                '}';
    }
}
