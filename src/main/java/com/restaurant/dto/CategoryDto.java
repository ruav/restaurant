package com.restaurant.dto;

import java.util.ArrayList;
import java.util.List;

public class CategoryDto {

    private long id;
    private String name;

    private List<DishDto> dishes = new ArrayList<>();

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

    public List<DishDto> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishDto> dishes) {
        this.dishes = dishes;
    }
}
