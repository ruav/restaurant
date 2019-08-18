package com.restaurant.service;

import com.restaurant.entity.Desk;
import com.restaurant.repository.DeskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeskService extends AbstractService<DeskRepository, Desk> {

    @Autowired
    DeskRepository deskRepository;

    @Override
    DeskRepository repository() {
        return deskRepository;
    }

    public List<Desk> findByHall(long hallId) {
        return repository().findByHall(hallId);
    }

    public List<Desk> findAllByLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
        return repository().findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, offset, limit);
    }

}
