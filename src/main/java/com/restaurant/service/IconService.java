package com.restaurant.service;

import com.restaurant.entity.Icon;
import com.restaurant.repository.IconRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IconService extends AbstractService<IconRepository, Icon> {

    @Autowired
    IconRepository iconRepository;

    @Override
    IconRepository repository() {
        return iconRepository;
    }

    public Icon getPhotoByUrl(String url) {
        return repository().getIconByUrl(url);
    }

}
