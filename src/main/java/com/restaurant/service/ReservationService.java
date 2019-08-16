package com.restaurant.service;

import com.restaurant.entity.Client;
import com.restaurant.entity.Reservation;
import com.restaurant.entity.Restaurant;
import com.restaurant.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService extends AbstractService<ReservationRepository, Reservation>{

    @Autowired
    ReservationRepository repository;

    @Override
    ReservationRepository repository() {
        return repository;
    }

    public List<Reservation> findAllByLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
//        List<Client> list = repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to);
//        limit = (list.size() - offset) >= limit ? limit : list.size() - offset;
//        return repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to).subList(offset, limit);
        return repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, offset, limit);
    }
}
