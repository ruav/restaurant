package com.restaurant.repository;

import com.restaurant.entity.Hostess;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HostessRepository extends CrudRepository<Hostess, Long> {

    List<Hostess> findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(
            @Param("restaurantId") long restaurantId,
            @Param("from") long from,
            @Param("to") long to,
            Pageable pageable);

}