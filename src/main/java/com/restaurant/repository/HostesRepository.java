package com.restaurant.repository;

import com.restaurant.entity.Hostes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HostesRepository extends CrudRepository<Hostes, Long> {

    List<Hostes> findAllByLastChangeBetweenOrderByLastChangeAsc(long from, long to);

}