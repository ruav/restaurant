package com.restaurant.utils;

import com.restaurant.dto.CategoryDto;
import com.restaurant.dto.DishDto;
import com.restaurant.dto.PhotoDto;
import com.restaurant.entity.Category;
import com.restaurant.entity.Dish;
import com.restaurant.entity.Photo;

import java.util.List;

public class Converter {


    public static CategoryDto getCategoryDto(Category category, List<Dish> dishes, String url) {
        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());

        dishes.forEach(dish -> categoryDto.getDishes().add(getDishDto(dish, url)));

        return categoryDto;
    }


    public static DishDto getDishDto(Dish dish, String url) {
        DishDto dishDto = new DishDto();

        dishDto.setId(dish.getId());
        dishDto.setName(dish.getName());
        dishDto.setPrice(dish.getPrice());
        dishDto.setPhoto(dish.getPhotos().isEmpty() ? null : url + dish.getPhotos().stream().findFirst().get().getUrl());

        return dishDto;

    }

    public static PhotoDto getPhotoDto(Photo photo, String url) {
        return new PhotoDto(url + photo.getUrl());
    }

}
