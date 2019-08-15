package com.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Set;

public class ClientDto {

    @ApiModelProperty
    private long id;
    @ApiModelProperty
    private String name;
    @ApiModelProperty
    private String phone;
    @ApiModelProperty
    private List<Long> tags;
    @ApiModelProperty
    private boolean vip;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Long> getTags() {
        return tags;
    }

    public void setTags(List<Long> tags) {
        this.tags = tags;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public long getLastChange() {
        return lastChange;
    }

    public void setLastChange(long lastChange) {
        this.lastChange = lastChange;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", tags=" + tags +
                ", work=" + vip +
                ", lastChange=" + lastChange +
                '}';
    }
}
