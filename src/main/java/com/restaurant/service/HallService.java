package com.restaurant.service;

import com.restaurant.entity.Hall;
import com.restaurant.repository.HallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HallService extends AbstractService<HallRepository, Hall> {

    @Autowired
    HallRepository repository;

    @Override
    HallRepository repository() {
        return repository;
    }

    public List<Hall> findByRestaurantId(long restaurantId) {
        return repository.findByRestaurantId(restaurantId);
    }

    public List<Hall> findAllByLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
//        List<Client> list = repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to);
//        limit = (list.size() - offset) >= limit ? limit : list.size() - offset;
//        return repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to).subList(offset, limit);
        return repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, offset, limit);
    }
}
