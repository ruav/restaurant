package com.restaurant.service;

import com.restaurant.entity.Restaurant;
import com.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService extends AbstractService<RestaurantRepository, Restaurant>{

    @Autowired
    RestaurantRepository repository;

    @Override
    RestaurantRepository repository() {
        return repository;
    }

    @Override
    public List<Restaurant> findAll() {
        return (List<Restaurant>) repository().findAll();
    }

}
