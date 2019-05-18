package com.restaurant.repository;

import com.restaurant.entity.Protein;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProteinRepository extends CrudRepository<Protein, Long> {

}