package com.restaurant.rest;

import com.restaurant.dto.CategoryDto;
import com.restaurant.entity.Category;
import com.restaurant.entity.Dish;
import com.restaurant.entity.Restaurant;
import com.restaurant.entity.SubCategory;
import com.restaurant.service.CategoryService;
import com.restaurant.service.DishService;
import com.restaurant.service.RestaurantService;
import com.restaurant.service.SubCategoryService;
import com.restaurant.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest")
public class api {

    @Autowired
    CategoryService categoryService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    DishService dishService;

    @Autowired
    SubCategoryService subCategoryService;

    @Autowired
    Converter converter;

    @RequestMapping(value = "/restaurant/{id}")
    public List<Object> restaurant(@PathVariable("id") long restaurantId, HttpServletRequest request) {
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/image/";

        List<Object> objects = new ArrayList<>();

        Restaurant restaurant = restaurantService.findById(restaurantId).get();
        Map<String, String> mapLogo = new HashMap<>();
        mapLogo.put("logo", restaurant.getLogo() == null ? null : restaurant.getLogo().getUrl());
        objects.add(mapLogo);
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categoryService.findByRestaurant(restaurantId)) {

            List<SubCategory> subCategories = subCategoryService.findByCategoryId(category.getId());
            Map<Long, List<Dish>> map = subCategories.stream().collect(Collectors.toMap(SubCategory::getId, subCategory -> dishService.findBySubCategoryId(subCategory.getId()), (a, b) -> b));
            CategoryDto categoryDto = Converter.getCategoryDto(category, subCategories,
                    map, url);
            categoryDtos.add(categoryDto);
        }
        objects.add(categoryDtos);

        return objects;
    }


}
