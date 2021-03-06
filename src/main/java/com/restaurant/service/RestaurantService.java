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

    public List<Restaurant> findByCoordinate() {
        return findByCoordinate(null, null, null, null);
    }

    public List<Restaurant> findByCoordinate(Float latitude1, Float longtitude1, Float lanitude2, Float longtitude2 ) {
        if (latitude1 == null || lanitude2 == null || longtitude1 == null || longtitude2 == null) {
            return findAll();
        }
        return repository
//                .findByLatitudeIsGreaterThanEqualAndLongtitudeGreaterThanEqualAAndLatitudeLessThanEqualAndLongtitudeLessThanEqual(latitude1, longtitude1, lanitude2, longtitude2);
                .findByLatitudeGreaterThanEqualAndLongtitudeGreaterThanEqualAndLatitudeLessThanEqualAndLongtitudeLessThanEqual(latitude1, longtitude1, lanitude2, longtitude2);
    }

}
