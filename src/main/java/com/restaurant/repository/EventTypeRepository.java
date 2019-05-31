package com.restaurant.repository;

import com.restaurant.entity.Event;
import com.restaurant.entity.EventType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTypeRepository extends CrudRepository<EventType, Long> {

}