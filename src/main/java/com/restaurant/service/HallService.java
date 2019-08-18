package com.restaurant.service;

import com.restaurant.entity.Hall;
import com.restaurant.repository.HallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HallService extends AbstractService<HallRepository, Hall> {

    @Autowired
    HallRepository hallRepository;

    @Override
    HallRepository repository() {
        return hallRepository;
    }

    public List<Hall> findByRestaurantId(long restaurantId) {
        return repository().findByRestaurantId(restaurantId);
    }

    public List<Hall> findAllByLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
        return repository().findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, offset, limit);
    }
}
