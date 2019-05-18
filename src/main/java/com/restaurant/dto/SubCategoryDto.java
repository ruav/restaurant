package com.restaurant.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubCategoryDto implements Serializable {

    private long id;
    private String name;

    private String photo;

    List<DishDto> dishes = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<DishDto> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishDto> dishes) {
        this.dishes = dishes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
