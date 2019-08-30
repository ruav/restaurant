package com.restaurant.service;

import com.restaurant.entity.Hall;
import com.restaurant.repository.HallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    public List<Hall> findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
        if (offset%limit != 0) throw new IllegalArgumentException("Offset must be a multiple of limit");
        return repository().findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(restaurantId, from, to, PageRequest.of(offset/limit, limit));
    }
}
