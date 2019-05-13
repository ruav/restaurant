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
}
