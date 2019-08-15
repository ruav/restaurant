package com.restaurant.service;

import com.restaurant.entity.Desk;
import com.restaurant.repository.DeskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeskService extends AbstractService<DeskRepository, Desk> {

    @Autowired
    DeskRepository repository;

    @Override
    DeskRepository repository() {
        return repository;
    }

    public List<Desk> findByHall(long hallId) {
        return repository.findByHall(hallId);
    }

    public List<Desk> findAllByLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
//        List<Client> list = repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to);
//        limit = (list.size() - offset) >= limit ? limit : list.size() - offset;
//        return repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to).subList(offset, limit);
        return repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, offset, limit);
    }

}
