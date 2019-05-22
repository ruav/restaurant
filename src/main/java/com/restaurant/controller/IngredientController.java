package com.restaurant.controller;

import com.restaurant.entity.Ingredient;
import com.restaurant.service.IconService;
import com.restaurant.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@Controller
@RequestMapping("/ingredients")
public class IngredientController extends AbstractController<IngredientService, Ingredient> {

    @Autowired
    IngredientService ingredientService;

    @Autowired
    IconService iconService;

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
        model.addAttribute("restaurantId", getHttpSession().getAttribute("restaurant"));
        model.addAttribute("ingredients", ingredientService.findAll());
        model.addAttribute("icons", iconService.findAll());
        model.addAttribute("restaurantId", (Long) getHttpSession().getAttribute("restaurant"));

        return prefix() + "/add";
    }

    @Override
    @PostMapping("/add")
    public String add(@Valid Ingredient entity, BindingResult result, Model model) {
        if (result.hasErrors() ) {
            model.addAttribute("ingredients", ingredientService.findAll());
            model.addAttribute("restaurantId", (Long) getHttpSession().getAttribute("restaurant"));
            return prefix() + "/add";
        }
        try {
            repository().save(entity);
        } catch (Exception e) {
            result.addError(new ObjectError("Error", "Такой элемент уже существует"));
            model.addAttribute("restaurantId", (Long) getHttpSession().getAttribute("restaurant"));
            model.addAttribute("ingredients", ingredientService.findAll());
            return prefix() + "/add";
        }
        if(getHttpSession().getAttribute("back") == null) {
            model.addAttribute("list", repository().findAll());
        }
        return "redirect:" + prefix() + "/add?id=" + getHttpSession().getAttribute("restaurant");
    }

    @Override
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) throws Throwable {
        Ingredient entity = repository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        model.addAttribute("restaurantId", (Long) getHttpSession().getAttribute("restaurant"));
        model.addAttribute("ingredients", ingredientService.findAll());
        model.addAttribute("icons", iconService.findAll());
        model.addAttribute("ingredient", entity);
        return  prefix() + "/update";
    }

    @Override
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Ingredient entity,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            entity.setId(id);
            model.addAttribute("restaurantId", getHttpSession().getAttribute("restaurant"));
            model.addAttribute("entity", entity);
            return prefix() + "/update";
        }
        try {
            repository().save(entity);
        } catch (Exception e) {
            entity.setId(id);
            model.addAttribute("restaurantId", getHttpSession().getAttribute("restaurant"));
            model.addAttribute("entity", entity);
            result.addError(new ObjectError("error", "Ошибка сохранения. Такой элемент уже существует"));
            return prefix() + "/update";
        }
        if(getHttpSession().getAttribute("back") == null) {
            model.addAttribute("list", repository().findAll());
        }
        return "redirect:" + prefix() + "/edit/" + entity.getId();
    }



}
