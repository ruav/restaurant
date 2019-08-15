package com.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class ReplacementDto {

    @ApiModelProperty
    private Date when;
    @ApiModelProperty
    private int tableFrom;
    @ApiModelProperty
    private int tableTo;

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public int getTableFrom() {
        return tableFrom;
    }

    public void setTableFrom(int tableFrom) {
        this.tableFrom = tableFrom;
    }

    public int getTableTo() {
        return tableTo;
    }

    public void setTableTo(int tableTo) {
        this.tableTo = tableTo;
    }

    @Override
    public String toString() {
        return "ReplacementDto{" +
                "when=" + when +
                ", tableFrom=" + tableFrom +
                ", tableTo=" + tableTo +
                '}';
    }
}
