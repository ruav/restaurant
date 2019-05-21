package com.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class RestaurantModel {
    @ApiModelProperty
    private String logo;

    @ApiModelProperty
    private List<AllergenDto> allergens;

    @ApiModelProperty
    List<ProteinDto> proteins;

    @ApiModelProperty
    List<CategoryDto> categoryDtos;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<AllergenDto> getAllergens() {
        return allergens;
    }

    public void setAllergens(List<AllergenDto> allergens) {
        this.allergens = allergens;
    }

    public List<ProteinDto> getProteins() {
        return proteins;
    }

    public void setProteins(List<ProteinDto> proteins) {
        this.proteins = proteins;
    }

    public List<CategoryDto> getCategoryDtos() {
        return categoryDtos;
    }

    public void setCategoryDtos(List<CategoryDto> categoryDtos) {
        this.categoryDtos = categoryDtos;
    }
}
