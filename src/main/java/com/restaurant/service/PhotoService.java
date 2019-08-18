package com.restaurant.service;

import com.restaurant.entity.Photo;
import com.restaurant.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService extends AbstractService<PhotoRepository, Photo> {

    @Autowired
    PhotoRepository photoRepository;

    @Override
    PhotoRepository repository() {
        return photoRepository;
    }

    public Photo getPhotoByUrl(String url) {
        return repository().getPhotoByUrl(url);
    }

}
