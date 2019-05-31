package com.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;

public class AllergenDto  {

    @ApiModelProperty
    private long id;

    @ApiModelProperty
    private String name;

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

}
