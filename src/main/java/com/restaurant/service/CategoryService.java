package com.restaurant.service;

import com.restaurant.entity.Category;
import com.restaurant.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService extends AbstractService<CategoryRepository, Category> {

    @Autowired
    CategoryRepository repository;

    @Override
    CategoryRepository repository() {
        return repository;
    }

    public List<Category> findByRestaurant(long restaurantId) {
        return repository.findByRestaurantId(restaurantId);
    }
}
