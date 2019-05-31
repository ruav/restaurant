package com.restaurant.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import java.io.Serializable;

public class PasswordForgotDto implements Serializable {

    @Email
    @NotEmpty
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}