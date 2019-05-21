package com.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryDto implements Serializable {

    @ApiModelProperty
    private long id;

    @ApiModelProperty
    private String name;

    @ApiModelProperty
    private List<SubCategoryDto> subCategories = new ArrayList<>();

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

    public List<SubCategoryDto> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategoryDto> subCategories) {
        this.subCategories = subCategories;
    }
}
