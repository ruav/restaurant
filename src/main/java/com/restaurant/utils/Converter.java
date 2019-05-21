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
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Converter {

    public static CategoryDto getCategoryDto(Category category, List<SubCategory> subCategories, Map<Long, List<Dish>> map, String url) {
        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());

        subCategories.forEach(s -> categoryDto.getSubCategories().add(getSubCategoryDto(s, map.get(s.getId()), url)));
        return categoryDto;
    }

    public static SubCategoryDto getSubCategoryDto(SubCategory s, List<Dish> dishCollection, String url) {
        SubCategoryDto dto = new SubCategoryDto();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setPhoto(s.getPhotos().isEmpty() ? null : url + s.getPhotos().stream().findFirst().get().getUrl());
        List<DishDto> dishes = dishCollection.stream().map(dish -> getDishDto(dish, url)).collect(Collectors.toList());
        dto.setDishes(dishes);
        return dto;
    }


    public static DishDto getDishDto(Dish dish, String url) {
        DishDto dto = new DishDto();

        dto.setId(dish.getId());
        dto.setPrice(dish.getPrice());
        dto.setName(dish.getName());
        dto.setPrice(dish.getPrice());
        dto.setPhoto(dish.getPhotos().isEmpty() ? null : url + dish.getPhotos().stream().findFirst().get().getUrl());
        dto.setIngredients(dish.getIngredients().stream().map(i -> getIngredientDto(i, url)).collect(Collectors.toList()));
        List<Long> allergens = dish.getAllergens().stream().map(Allergen::getId).collect(Collectors.toList());
        List<Long> proteins = dish.getProteins().stream().map(Protein::getId).collect(Collectors.toList());

        dto.setProteins(proteins);
        dto.setAllergens(allergens);
        return dto;

    }

    public static IngredientDto getIngredientDto(Ingredient ingredient, String url) {
        IngredientDto dto = new IngredientDto();
        dto.setId(ingredient.getId());
        dto.setName(ingredient.getName());
        dto.setIcon(ingredient.getIcon() == null ? null : url + ingredient.getIcon().getUrl());
        return dto;
    }

    public static ProteinDto getProteinDto(Protein protein) {
        ProteinDto dto = new ProteinDto();
        dto.setId(protein.getId());
        dto.setName(protein.getName());

        return dto;
    }

    public static AllergenDto getAllergenDto(Allergen allergen) {
        AllergenDto dto = new AllergenDto();
        dto.setId(allergen.getId());
        dto.setName(allergen.getName());
        return dto;
    }

}
