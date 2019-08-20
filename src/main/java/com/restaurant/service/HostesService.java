package com.restaurant.service;

import com.restaurant.entity.Hostess;
import com.restaurant.repository.HostesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostesService extends AbstractService<HostesRepository, Hostess> {

    @Autowired
    HostesRepository hostesRepository;

    @Override
    HostesRepository repository() {
        return hostesRepository;
    }

    public List<Hostess> findAllByLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
        return repository().findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, offset, limit);
    }

}
