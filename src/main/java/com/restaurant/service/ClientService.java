package com.restaurant.service;

import com.restaurant.entity.Client;
import com.restaurant.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService extends AbstractService<ClientRepository, Client> {

    @Autowired
    ClientRepository repository;

    @Override
    ClientRepository repository() {
        return repository;
    }

    public List<Client> findAllByLastChangeBetweenOrderByLastChangeAsc(long from, long to, long restaurantId, int limit, int offset) {
        List<Client> list = repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to);
        limit = (list.size() - offset) >= limit ? limit : list.size() - offset;
        return repository.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to).subList(offset, limit);
    }


}
