package com.restaurant.service;

import com.restaurant.entity.SubCategory;
import com.restaurant.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubCategoryService extends AbstractService<SubCategoryRepository, SubCategory> {

    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Override
    SubCategoryRepository repository() {
        return subCategoryRepository;
    }

    public List<SubCategory> findByCategoryId(long categoryId) {
        return repository().findByCategoryId(categoryId);
    }

    public List<SubCategory> findActiveByCategoryId(long categoryId) {
        return repository().findByCategoryId(categoryId).stream().filter(SubCategory::isActive).collect(Collectors.toList());
    }
}
