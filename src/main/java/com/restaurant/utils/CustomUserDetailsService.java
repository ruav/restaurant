package com.restaurant.utils;

import com.restaurant.entity.User;
import com.restaurant.service.UserService;
import com.restaurant.vo.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        User userIn = null;
        try {
            userIn = userRepository.findByEmail(username);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }

        if (userIn == null) return null;

        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        if(userIn.getId() != 0) {
            if (Role.ROOT == userIn.getRole()) {
                authList.add(new SimpleGrantedAuthority(Role.ROOT.name()));
            } else if (Role.SYSTEM == userIn.getRole()){
                authList.add(new SimpleGrantedAuthority(Role.SYSTEM.name()));
            }
        }
        UserDetails user = new org.springframework.security.core.userdetails.User(username, userIn.getPass(), authList);

        return user;
    }
}