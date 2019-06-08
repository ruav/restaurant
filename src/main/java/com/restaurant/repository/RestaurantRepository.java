package com.restaurant.repository;

import com.restaurant.entity.Restaurant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

//    List<Restaurant> findByLatitudeIsGreaterThanEqualAndLongtitudeGreaterThanEqualAAndLatitudeLessThanEqualAndLongtitudeLessThanEqual(Float latitude1, Float longtitude1, Float lanitude2, Float longtitude2);
    List<Restaurant> findByLatitudeGreaterThanEqualAndLongtitudeGreaterThanEqualAndLatitudeLessThanEqualAndLongtitudeLessThanEqual(Float latitude1, Float longtitude1, Float lanitude2, Float longtitude2);
//    List<Restaurant> findByLatitudeIsGreaterThanEqualAndLongtitudeGreaterThanEqualAAndLatitudeLessThanEqualAndLongtitudeLessThanEqual(Float latitude1, Float longtitude1, Float lanitude2, Float longtitude2);

}