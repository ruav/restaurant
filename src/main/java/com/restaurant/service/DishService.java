package com.restaurant.service;

import com.restaurant.entity.Dish;
import com.restaurant.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService extends AbstractService<DishRepository, Dish> {

    @Autowired
    DishRepository repository;

    @Override
    DishRepository repository() {
        return repository;
    }

    public List<Dish> findByCategoryId(long categoryId) {
        return repository.findByCategoryId(categoryId);
    }
}
