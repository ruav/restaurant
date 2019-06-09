package com.restaurant.service;

import com.restaurant.entity.Ingredient;
import com.restaurant.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService extends AbstractService<IngredientRepository, Ingredient> {

    @Autowired
    IngredientRepository repository;

    @Override
    IngredientRepository repository() {
        return repository;
    }

    public List<Ingredient> findByNameIsLike (String name, Pageable pageable) {
        return repository.findByNameIgnoreCaseContaining(name , pageable);
    }

    public List<Ingredient> findAll(Pageable pageable) {
        return  repository.findAll(pageable).getContent();
    }

    public Ingredient findByName(String name) {
        return repository.findByNameIgnoreCase(name);
    }

}
