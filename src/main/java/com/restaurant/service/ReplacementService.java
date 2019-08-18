package com.restaurant.service;

import com.restaurant.entity.Replacement;
import com.restaurant.repository.ReplacementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplacementService extends AbstractService<ReplacementRepository, Replacement> {

    @Autowired
    ReplacementRepository replacementRepository;

    @Override
    ReplacementRepository repository() {
        return replacementRepository;
    }

}
