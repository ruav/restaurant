package com.restaurant.service;

import com.restaurant.entity.EventType;
import com.restaurant.repository.EventTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventTypeService extends AbstractService<EventTypeRepository, EventType> {

    @Autowired
    EventTypeRepository eventTypeRepository;

    @Override
    EventTypeRepository repository() {
        return eventTypeRepository;
    }

}
