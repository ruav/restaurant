package com.restaurant.utils;

import com.restaurant.dto.AllergenDto;
import com.restaurant.dto.CategoryDto;
import com.restaurant.dto.DishDto;
import com.restaurant.dto.IngredientDto;
import com.restaurant.dto.ProteinDto;
import com.restaurant.dto.SubCategoryDto;
import com.restaurant.entity.Allergen;
import com.restaurant.entity.Category;
import com.restaurant.entity.Dish;
import com.restaurant.entity.Ingredient;
import com.restaurant.entity.Protein;
import com.restaurant.entity.SubCategory;
import com.restaurant.service.DishService;
import com.restaurant.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Converter {

    @Autowired
    DishService dishService;

    public  CategoryDto getCategoryDto(Category category, List<SubCategory> subCategories, String url) {
        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());

        subCategories.forEach(s -> categoryDto.getSubCategories().add(getSubCategoryDto(s, url)));
        return categoryDto;
    }

    public  SubCategoryDto getSubCategoryDto(SubCategory s, String url) {
        SubCategoryDto dto = new SubCategoryDto();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setPhoto(s.getPhotos().isEmpty() ? null : url + s.getPhotos().stream().findFirst().get().getUrl());
        List<DishDto> dishes = dishService.findBySubCategoryId(s.getCategoryId()).stream().map(d -> getDishDto(d, url)).collect(Collectors.toList());
        dto.setDishes(dishes);
        return dto;
    }


    public  DishDto getDishDto(Dish dish, String url) {
        DishDto dto = new DishDto();

        dto.setId(dish.getId());
        dto.setPrice(dish.getPrice());
        dto.setName(dish.getName());
        dto.setPrice(dish.getPrice());
        dto.setPhoto(dish.getPhotos().isEmpty() ? null : url + dish.getPhotos().stream().findFirst().get().getUrl());
        dto.setIngredients(dish.getIngredients().stream().map(i -> ingredientDto(i, url)).collect(Collectors.toList()));
        dto.setProteins(dish.getProteins().stream().map(p -> proteinDto(p)).collect(Collectors.toList()));
        dto.setAllergens(dish.getAllergens().stream().map(a -> allergenDto(a)).collect(Collectors.toList()));
        return dto;

    }

    public IngredientDto ingredientDto(Ingredient ingredient, String url) {
        IngredientDto dto = new IngredientDto();
        dto.setId(ingredient.getId());
        dto.setName(ingredient.getName());
        dto.setIcon(ingredient.getIcon() == null ? null : url + ingredient.getIcon().getUrl());
        return dto;
    }

    public ProteinDto proteinDto(Protein protein) {
        ProteinDto dto = new ProteinDto();
        dto.setId(protein.getId());
        dto.setName(protein.getName());

        return dto;
    }

    public AllergenDto allergenDto(Allergen allergen) {
        AllergenDto dto = new AllergenDto();
        dto.setId(allergen.getId());
        dto.setName(allergen.getName());
        return dto;
    }

}
