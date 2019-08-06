package com.restaurant.service;

import com.restaurant.entity.Hostes;
import com.restaurant.repository.HostesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostesService extends AbstractService<HostesRepository, Hostes> {

    @Autowired
    HostesRepository repository;

    @Override
    HostesRepository repository() {
        return repository;
    }

    public List<Hostes> findAllByLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
        List<Hostes> list = repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to);
        limit = (list.size() - offset) >= limit ? limit : list.size() - offset;
        return repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to).subList(offset, limit);
    }

}
