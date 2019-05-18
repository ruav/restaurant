package com.restaurant.repository;

import com.restaurant.entity.SubCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends CrudRepository<SubCategory, Long> {

    List<SubCategory> findByCategoryId(long categoryId);

}