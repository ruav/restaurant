package com.restaurant.controller;

import com.restaurant.entity.Category;
import com.restaurant.entity.Photo;
import com.restaurant.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.UUID;

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

    @Override
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Category entity,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            entity.setId(id);
            model.addAttribute("restaurantId", getHttpSession().getAttribute("restaurant"));
            model.addAttribute("entity", entity);
            return prefix() + "/update";
        }
        entity.setLogo(repository().findById(entity.getId()).get().getLogo());
        try {
            repository().save(entity);
        } catch (Exception e) {
            entity.setId(id);
            model.addAttribute("restaurantId", getHttpSession().getAttribute("restaurant"));
            model.addAttribute("entity", entity);
            result.addError(new ObjectError("error", "Ошибка сохранения. Такой элемент уже существует"));
            return prefix() + "/update";
        }
        if (getHttpSession().getAttribute("back") == null) {
            model.addAttribute("list", repository().findAll());
        }
        return getHttpSession().getAttribute("back") != null ? "redirect:" + getHttpSession().getAttribute("back") : prefix() + "/list";
    }


}
