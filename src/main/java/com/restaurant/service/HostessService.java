package com.restaurant.service;

import com.restaurant.entity.Hostess;
import com.restaurant.repository.HostessRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Hostess> findAllByLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
        return repository().findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, offset, limit);
    }

}
