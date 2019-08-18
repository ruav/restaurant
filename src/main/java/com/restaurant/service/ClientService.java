package com.restaurant.service;

import com.restaurant.entity.Client;
import com.restaurant.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Client> findAllByLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
        return repository().findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, offset, limit);
    }


}
