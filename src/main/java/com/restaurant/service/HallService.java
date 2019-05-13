package com.restaurant.service;

import com.restaurant.entity.Hall;
import com.restaurant.repository.HallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HallService extends AbstractService<HallRepository, Hall> {

    @Autowired
    HallRepository repository;

    @Override
    HallRepository repository() {
        return repository;
    }

    public List<Hall> findByRestaurantId(long restaurantId) {
        return repository.findByRestaurantId(restaurantId);
    }
}
