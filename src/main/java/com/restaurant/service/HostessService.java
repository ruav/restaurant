package com.restaurant.service;

import com.restaurant.entity.Hostess;
import com.restaurant.repository.HostessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostessService extends AbstractService<HostessRepository, Hostess> {

    @Autowired
    HostessRepository hostessRepository;

    @Override
    HostessRepository repository() {
        return hostessRepository;
    }

    public List<Hostess> findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(long restaurantId, long from, long to, int limit, int offset) {
        if (offset%limit != 0) throw new IllegalArgumentException("Offset must be a multiple of limit");
        return repository().findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(restaurantId, from, to, PageRequest.of(offset/limit, limit));
    }

}
