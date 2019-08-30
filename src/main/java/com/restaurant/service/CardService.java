package com.restaurant.service;

import com.restaurant.entity.Card;
import com.restaurant.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    public List<Card> findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
        if (offset%limit != 0) throw new IllegalArgumentException("Offset must be a multiple of limit");
        return repository().findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(restaurantId, from, to, PageRequest.of(offset/limit, limit));
    }


}
