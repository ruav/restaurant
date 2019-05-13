package com.restaurant.rest;

import com.restaurant.dto.CategoryDto;
import com.restaurant.dto.PhotoDto;
import com.restaurant.entity.Photo;
import com.restaurant.service.CategoryService;
import com.restaurant.service.DishService;
import com.restaurant.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest")
public class api {

    @Autowired
    CategoryService categoryService;

    @Autowired
    DishService dishService;

    @RequestMapping(value ="/menu/{id}")
    public List<CategoryDto> menu(@PathVariable("id") long restaurantId, HttpServletRequest request) {

        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/image/";

        List<CategoryDto> categoryDtos = categoryService
                .findByRestaurant(restaurantId)
                .stream()
                .map(category -> Converter.getCategoryDto(category, dishService.findByCategoryId(category.getId()), url))
                .collect(Collectors.toList());

        return categoryDtos;

    }

    @RequestMapping(value ="/dish/{id}")
    public List<PhotoDto> dishes(@PathVariable("id") long dishId,
                                 HttpServletRequest request) {
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/image/";

        List<PhotoDto> dtos = dishService.findById(dishId).get()
                .getPhotos()
                .stream()
                .map((Photo photo) -> Converter.getPhotoDto(photo, url))
                .collect(Collectors.toList());

        return dtos;
    }

}
