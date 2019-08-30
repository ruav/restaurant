package com.restaurant.service;

import com.restaurant.entity.Client;
import com.restaurant.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService extends AbstractService<ClientRepository, Client> {

    @Autowired
    ClientRepository clientRepository;

    @Override
    ClientRepository repository() {
        return clientRepository;
    }

    public List<Client> findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
        if (offset%limit != 0) throw new IllegalArgumentException("Offset must be a multiple of limit");
        return repository().findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(restaurantId, from, to, PageRequest.of(offset/limit, limit));
    }


}
