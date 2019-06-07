package com.restaurant.service;

import com.restaurant.entity.Dish;
import com.restaurant.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishService extends AbstractService<DishRepository, Dish> {

    @Autowired
    DishRepository repository;

    @Override
    DishRepository repository() {
        return repository;
    }

    public List<Dish> findBySubCategoryId(long subCategoryId) {
        return repository.findBySubCategoryId(subCategoryId);
    }

    public List<Dish> findActiveBySubCategoryId(long subCategoryId) {
        return repository.findBySubCategoryId(subCategoryId).stream().filter(Dish::isActive).collect(Collectors.toList());
    }
}
