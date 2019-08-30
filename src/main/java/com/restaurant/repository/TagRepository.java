package com.restaurant.repository;

import com.restaurant.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

    List<Tag> findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(
            @Param("restaurantId") long restaurantId,
            @Param("from") long from,
            @Param("to") long to,
            Pageable pageable);


}