package com.restaurant.repository;

import com.restaurant.entity.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

    List<Tag> findAllByLastChangeBetweenOrderByLastChangeAsc(long from, long to);


}