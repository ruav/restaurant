package com.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class RestaurantMenuModel {
    @ApiModelProperty
    private String logo;

    @ApiModelProperty
    List<String> actions;

    @ApiModelProperty
    private List<AllergenDto> allergens;

    @ApiModelProperty
    List<ProteinDto> proteins;

    @ApiModelProperty
    List<CategoryDto> categories;

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

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }
}
