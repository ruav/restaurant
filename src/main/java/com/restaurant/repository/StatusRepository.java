package com.restaurant.repository;

import com.restaurant.entity.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends CrudRepository<Status, Long> {

    Status findFirstByReservationOOrderByLastChangeDesc(long reservationId);

}