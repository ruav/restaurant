package com.restaurant.service;

import com.restaurant.entity.SubCategory;
import com.restaurant.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryService extends AbstractService<SubCategoryRepository, SubCategory> {

    @Autowired
    SubCategoryRepository repository;

    @Override
    SubCategoryRepository repository() {
        return repository;
    }

    public List<SubCategory> findByCategoryId(long categoryId) {
        return repository.findByCategoryId(categoryId);
    }
}
