package com.restaurant.controller;

import com.restaurant.entity.Ingredient;
import com.restaurant.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.websocket.server.PathParam;

@Controller
@RequestMapping("/ingredients")
public class IngredientController extends AbstractController<IngredientService, Ingredient> {

    @Autowired
    IngredientService ingredientService;

    @Override
    String prefix() {
        return "/ingredients";
    }

    @Override
    IngredientService repository() {
        return ingredientService;
    }

    @Override
    @GetMapping("/add")
    public String showSignUpForm(Ingredient entity, @PathParam(value ="id") long id, Model model) {
        model.addAttribute("restaurantId", id);
        model.addAttribute("ingredients", ingredientService.findAll());
        return prefix() + "/add";
    }
}
