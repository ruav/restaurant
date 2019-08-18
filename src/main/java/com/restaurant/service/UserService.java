package com.restaurant.service;

import com.restaurant.entity.Restaurant;
import com.restaurant.entity.User;
import com.restaurant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService extends AbstractService<UserRepository, User> {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    UserRepository repository() {
        return userRepository;
    }

    public Set<Restaurant> findRestaurant(long id) {
        Optional<User> user = repository().findById(id);
        if (user.isPresent()) {
            return user.get().getRestaurants();
        }
        return Collections.emptySet();
    }

    public User addRestaurant(User user, Restaurant restaurant) {
        user.getRestaurants().add(restaurant);
        return repository().save(user);
    }

    public User findByEmail(String email) {
        return repository().findByEmail(email);
    }

    public User add(User user) {
        user.setPass(passwordEncoder.encode(user.getPass()));
        return repository().save(user);
    }

    @Override
    public User save(User user){
        return repository().save(user);
    }

    public void updatePassword(String password, long userId) {
        Optional<User> user = repository().findById(userId);
        if (user.isPresent()) {
            user.get().setPass(passwordEncoder.encode(password));
            user.get().setRestaurants(findByEmail(user.get().getEmail()).getRestaurants());
            save(user.get());
        }
    }

    public void update(User user) {
        if(user.getPass() == null || user.getPass().isEmpty()) {
            user.setPass(findByEmail(user.getEmail()).getPass());
        } else {
            user.setPass(passwordEncoder.encode(user.getPass()));
        }
        if (user.getRestaurants().isEmpty()) {
            user.setRestaurants(findByEmail(user.getEmail()).getRestaurants());
        }
        repository().save(user);
    }
}
