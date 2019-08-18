package com.restaurant.service;

import com.restaurant.entity.Town;
import com.restaurant.repository.TownRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TownService extends AbstractService<TownRepository, Town> {

    @Autowired
    TownRepository townRepository;

    @Override
    TownRepository repository() {
        return townRepository;
    }

    public Map<Long, Town> findMapAll() {
        Map<Long, Town> map = new HashMap<>();
        for(Town town : repository().findAll()) {
            map.put(town.getId(), town);
        }
        return map;
    }

}
