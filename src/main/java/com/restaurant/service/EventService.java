package com.restaurant.service;

import com.restaurant.entity.Event;
import com.restaurant.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService extends AbstractService<EventRepository, Event> {

    @Autowired
    EventRepository eventRepository;

    @Override
    EventRepository repository() {
        return eventRepository;
    }

    public List<Event> findByRestaurantId (long restaurantId) {
        return repository().findByRestaurantId(restaurantId);
    }


}
