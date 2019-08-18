package com.restaurant.service;

import com.restaurant.entity.Reservation;
import com.restaurant.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Reservation> findAllByLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
        return repository().findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, offset, limit);
    }
}
