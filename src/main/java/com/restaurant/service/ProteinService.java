package com.restaurant.service;

import com.restaurant.entity.Protein;
import com.restaurant.repository.ProteinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProteinService extends AbstractService<ProteinRepository, Protein> {

    @Autowired
    ProteinRepository repository;

    @Override
    ProteinRepository repository() {
        return repository;
    }

}
