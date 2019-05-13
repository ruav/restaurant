package com.restaurant.repository;

import com.restaurant.entity.Hall;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HallRepository extends CrudRepository<Hall, Long> {

    List<Hall> findByRestaurantId(long restaurantId);

}