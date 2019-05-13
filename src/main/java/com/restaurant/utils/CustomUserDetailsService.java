package com.restaurant.utils;

import com.restaurant.entity.User;
import com.restaurant.service.UserService;
import com.restaurant.vo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        User userIn = null;
        try {
            userIn = userRepository.findByEmail(username);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        if(userIn.getId() != 0) {
            if (Role.Root == userIn.getRole()) {
                authList.add(new SimpleGrantedAuthority(Role.Root.name()));
            } else {
                authList.add(new SimpleGrantedAuthority(Role.system.name()));
            }
        }
        UserDetails user = new org.springframework.security.core.userdetails.User(username, userIn.getPass(), authList);

        return user;
    }
}