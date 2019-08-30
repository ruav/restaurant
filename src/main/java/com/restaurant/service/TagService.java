package com.restaurant.service;

import com.restaurant.entity.Tag;
import com.restaurant.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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


    public List<Tag> findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
        if (offset%limit != 0) throw new IllegalArgumentException("Offset must be a multiple of limit");
        return repository().findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(restaurantId, from, to, PageRequest.of(offset/limit, limit));
    }

}
