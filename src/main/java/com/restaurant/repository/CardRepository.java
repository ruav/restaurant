package com.restaurant.repository;

import com.restaurant.entity.Card;
import com.restaurant.entity.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends CrudRepository<Card, Long> {

    @Query(value = "select * from card " +
            "where last_change between :from and :to " +
            "and restaurant_id = :restaurantId " +
            "order by last_change asc " +
            "limit :limit " +
            "offset :offset ", nativeQuery = true)
    List<Card> findAllByLastChangeBetweenOrderByLastChangeAsc(
            @Param("from") long from,
            @Param("to") long to,
            @Param("restaurantId") long restaurantId,
            @Param("offset") int offset,
            @Param("limit") int limit);

}