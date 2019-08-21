package com.restaurant.service;

import com.restaurant.entity.Card;
import com.restaurant.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService extends AbstractService<CardRepository, Card> {

    @Autowired
    CardRepository cardRepository;

    @Override
    CardRepository repository() {
        return cardRepository;
    }

    public List<Card> findAllByLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
        return repository().findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, offset, limit);
    }


}
