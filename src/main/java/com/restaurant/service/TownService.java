package com.restaurant.service;

import com.restaurant.entity.Town;
import com.restaurant.repository.TownRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TownService extends AbstractService<TownRepository, Town> {

    @Autowired
    TownRepository repository;

    @Override
    TownRepository repository() {
        return repository;
    }

}
