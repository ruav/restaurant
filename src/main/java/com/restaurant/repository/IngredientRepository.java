package com.restaurant.repository;

import com.restaurant.entity.Ingredient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends PagingAndSortingRepository<Ingredient, Long> {

    Ingredient findByNameIgnoreCase(String name);
    List<Ingredient> findByNameIgnoreCaseContaining(String name, Pageable pageable);

}