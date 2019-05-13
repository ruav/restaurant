package com.restaurant.repository;

import com.restaurant.entity.Desk;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeskRepository extends CrudRepository<Desk, Long> {

    List<Desk> findByHall(long hallId);

}