package com.restaurant.service;

import com.restaurant.entity.Ingredient;
import com.restaurant.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientService extends AbstractService<IngredientRepository, Ingredient> {

    @Autowired
    IngredientRepository repository;

    @Override
    IngredientRepository repository() {
        return repository;
    }
}
