package com.restaurant.service;

import com.restaurant.entity.Card;
import com.restaurant.entity.Client;
import com.restaurant.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService extends AbstractService<CardRepository, Card> {

    @Autowired
    CardRepository repository;

    @Override
    CardRepository repository() {
        return repository;
    }

    public List<Card> findAllByLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
//        List<Client> list = repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to);
//        limit = (list.size() - offset) >= limit ? limit : list.size() - offset;
//        return repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to).subList(offset, limit);
        return repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, offset, limit);
    }


}
