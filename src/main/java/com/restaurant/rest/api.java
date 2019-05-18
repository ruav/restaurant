package com.restaurant.rest;

import com.restaurant.dto.CategoryDto;
import com.restaurant.dto.PhotoDto;
import com.restaurant.entity.Category;
import com.restaurant.entity.Photo;
import com.restaurant.entity.Restaurant;
import com.restaurant.service.CategoryService;
import com.restaurant.service.DishService;
import com.restaurant.service.RestaurantService;
import com.restaurant.service.SubCategoryService;
import com.restaurant.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(value ="/menu/{id}")
    public List<CategoryDto> menu(@PathVariable("id") long restaurantId, HttpServletRequest request) {

        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/image/";

        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categoryService.findByRestaurant(restaurantId)) {
            CategoryDto categoryDto = converter.getCategoryDto(category, subCategoryService.findByCategoryId(category.getId()), url);
            categoryDtos.add(categoryDto);
        }

        return categoryDtos;

    }

//    @RequestMapping(value ="/dish/{id}")
//    public List<PhotoDto> dishes(@PathVariable("id") long dishId,
//                                 HttpServletRequest request) {
//        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/image/";
//
//        List<PhotoDto> dtos = dishService.findById(dishId).get()
//                .getPhotos()
//                .stream()
//                .map((Photo photo) -> converter.getPhotoDto(photo, url))
//                .collect(Collectors.toList());
//
//        return dtos;
//    }

    @RequestMapping(value = "/restaurant/{id}")
    public List<Object> restaurant(@PathVariable("id") long restaurantId, HttpServletRequest request) {
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/image/";

        List<Object> objects = new ArrayList<>();
//        List<CategoryDto> categoryDtos = categoryService
//                .findByRestaurant(restaurantId)
//                .stream()
//                .map(category -> Converter.getCategoryDto(category, dishService.findBySubCategoryId(category.getId()), url))
//                .collect(Collectors.toList());

        Restaurant restaurant = restaurantService.findById(restaurantId).get();
        Map<String, String> mapLogo = new HashMap<>();
        mapLogo.put("logo", restaurant.getLogo() == null ? null : restaurant.getLogo().getUrl());
        objects.add(mapLogo);
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categoryService.findByRestaurant(restaurantId)) {
            CategoryDto categoryDto = converter.getCategoryDto(category, subCategoryService.findByCategoryId(category.getId()), url);
            categoryDtos.add(categoryDto);
        }
        objects.add(categoryDtos);

//        objects.addAll(
//                categoryService
//                .findByRestaurant(restaurantId)
//                .stream()
//                .map(category -> Converter.getCategoryDto(category, dishService.findBySubCategoryId(category.getId()), url))
//                .collect(Collectors.toList())
//        );
        return objects;
    }


}
