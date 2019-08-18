package com.restaurant.service;

import com.restaurant.entity.Tag;
import com.restaurant.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService extends AbstractService<TagRepository, Tag> {

    @Autowired
    TagRepository tagRepository;

    @Override
    TagRepository repository() {
        return tagRepository;
    }


    public List<Tag> findAllByLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
        return repository().findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, offset, limit);
    }

}
