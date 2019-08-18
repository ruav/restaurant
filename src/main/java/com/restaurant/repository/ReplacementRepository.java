package com.restaurant.repository;

import com.restaurant.entity.Replacement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplacementRepository extends CrudRepository<Replacement, Long> {

}