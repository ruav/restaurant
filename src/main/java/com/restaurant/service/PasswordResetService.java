package com.restaurant.service;

import com.restaurant.entity.PasswordResetToken;
import com.restaurant.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordResetService extends AbstractService<PasswordResetTokenRepository, PasswordResetToken> {

    @Autowired
    PasswordResetTokenRepository repository;

    @Override
    PasswordResetTokenRepository repository() {
        return repository;
    }

    public PasswordResetToken findByToken(String token) {
        return repository.findByToken(token);
    }

    public List<PasswordResetToken> findByUserId(long userId) {
        return repository.findByUserId(userId);
    }

}
