package com.restaurant.service;

import com.restaurant.entity.Reservation;
import com.restaurant.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService extends AbstractService<ReservationRepository, Reservation>{

    @Autowired
    ReservationRepository reservationRepository;

    @Override
    ReservationRepository repository() {
        return reservationRepository;
    }

    public List<Reservation> findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
        if (offset%limit != 0) throw new IllegalArgumentException("Offset must be a multiple of limit");
        return repository().findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(restaurantId, from, to, PageRequest.of(offset/limit, limit));    }
}
