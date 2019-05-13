package com.restaurant.repository;

import com.restaurant.entity.Photo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends CrudRepository<Photo, Long> {

    Photo getPhotoByUrl(String url);


}