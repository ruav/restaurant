package com.restaurant.controller;

import com.restaurant.entity.Category;
import com.restaurant.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.websocket.server.PathParam;

@Controller
@RequestMapping("/categories")
public class CategoryController extends AbstractController<CategoryService, Category>{

    @Autowired
    CategoryService repository;

    @Override
    CategoryService repository() {
        return repository;
    }

    @Override
    String prefix() {
        return "/categories";
    }

    @Override
    @GetMapping("/add")
    public String showSignUpForm(Category entity, @PathParam(value ="id") long id, Model model) {
        entity.setRestaurantId(id);
        model.addAttribute("restaurantId", id);
        return prefix() + "/add";
    }

}
