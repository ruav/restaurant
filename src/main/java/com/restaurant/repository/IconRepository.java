package com.restaurant.repository;

import com.restaurant.entity.Icon;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IconRepository extends CrudRepository<Icon, Long> {

    Icon getIconByUrl(String url);

}