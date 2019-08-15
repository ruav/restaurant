package com.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;

public class CallDto {

    @ApiModelProperty
    private boolean call;
    @ApiModelProperty(name = "hall_id")
    private String phone;

    public boolean isCall() {
        return call;
    }

    public void setCall(boolean call) {
        this.call = call;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "CallDto{" +
                "call=" + call +
                ", phone='" + phone + '\'' +
                '}';
    }
}
