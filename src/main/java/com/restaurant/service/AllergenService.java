package com.restaurant.service;

import com.restaurant.entity.Allergen;
import com.restaurant.repository.AllergenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AllergenService extends AbstractService<AllergenRepository, Allergen> {

    @Autowired
    AllergenRepository repository;

    @Override
    AllergenRepository repository() {
        return repository;
    }

}
