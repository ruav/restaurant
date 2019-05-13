package com.restaurant.service;

import com.restaurant.entity.Data;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public abstract class AbstractService<T extends CrudRepository<V, Long>, V extends Data>  {


    abstract T repository();

    public List<V> findAll() {
        return (List<V>) repository().findAll();
    }

    public V save(V data) {
        return repository().save(data);
    }

    public Optional<V> findById(long id) {
        return repository().findById(id);
    }

    public void delete(V data) {
        repository().delete(data);
    }

}
