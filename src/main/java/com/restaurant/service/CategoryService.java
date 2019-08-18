package com.restaurant.service;

import com.restaurant.entity.Category;
import com.restaurant.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService extends AbstractService<CategoryRepository, Category> {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    CategoryRepository repository() {
        return categoryRepository;
    }

    public List<Category> findByRestaurant(long restaurantId) {
        return repository().findByRestaurantId(restaurantId);
    }

    public List<Category> findActiveByRestaurant(long restaurantId) {
        return repository().findByRestaurantId(restaurantId).stream().filter(Category::isActive).collect(Collectors.toList());
    }
}
