package com.restaurant.service;

import com.restaurant.entity.Status;
import com.restaurant.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusService extends AbstractService<StatusRepository, Status> {

    @Autowired
    StatusRepository statusRepository;

    @Override
    StatusRepository repository() {
        return statusRepository;
    }

    public Status findFirstByReservationOOrderByLastChangeDesc(long reservationId) {
        return repository().findFirstByReservationOOrderByLastChangeDesc(reservationId);
    }

}
