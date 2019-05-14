package com.restaurant.service;

import com.restaurant.entity.Restaurant;
import com.restaurant.entity.User;
import com.restaurant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService extends AbstractService<UserRepository, User> {

    @Autowired
    UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    UserRepository repository() {
        return repository;
    }

    public Set<Restaurant> findRestaurant(long id) {
        return repository.findById(id).get().getRestaurants();
    }

    public User addRestaurant(User user, Restaurant restaurant) {
        user.getRestaurants().add(restaurant);
        return repository.save(user);
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User add(User user) {
        user.setPass(passwordEncoder.encode(user.getPass()));
        return repository.save(user);
    }

    @Override
    public User save(User user){
        return repository.save(user);
    }

    public void updatePassword(String password, long userId) {
        User user = repository.findById(userId).get();
        user.setPass(passwordEncoder.encode(password));
        user.setRestaurants(findByEmail(user.getEmail()).getRestaurants());
        save(user);
    }

    public void update(User user) {
        if(user.getPass() == null || user.getPass().isEmpty()) {
            user.setPass(findByEmail(user.getEmail()).getPass());
        } else {
            user.setPass(passwordEncoder.encode(user.getPass()));
        }
        user.setRestaurants(findByEmail(user.getEmail()).getRestaurants());
        repository.save(user);
    }
}
