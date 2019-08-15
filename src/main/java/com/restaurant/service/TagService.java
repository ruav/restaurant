package com.restaurant.service;

import com.restaurant.entity.Tag;
import com.restaurant.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService extends AbstractService<TagRepository, Tag> {

    @Autowired
    TagRepository repository;

    @Override
    TagRepository repository() {
        return repository;
    }


    public List<Tag> findAllByLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
//        List<Tag> list = repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to);
//        limit = (list.size() - offset) >= limit ? limit : list.size() - offset;
//        return repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to).subList(offset, limit);
        return repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, offset, limit);
    }

}
