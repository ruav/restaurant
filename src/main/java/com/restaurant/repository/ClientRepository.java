package com.restaurant.repository;

import com.restaurant.entity.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

    List<Client> findAllByLastChangeBetweenOrderByLastChangeAsc(long from, long to);


}