package com.restaurant.controller;

import com.restaurant.entity.Category;
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
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController extends AbstractController<CategoryService, Category>{

    @Autowired
    CategoryService categoryService;

    @Override
    CategoryService repository() {
        return categoryService;
    }

    @Override
    String prefix() {
        return "/categories";
    }

    @Override
    @GetMapping("/add")
    public String showSignUpForm(Category entity, @PathParam(value ="id") long id, Model model) {
        getCheckAccess().checkAccessRestaurant(getUser(), id);
        entity.setRestaurantId(id);
        entity.setActive(true);
        model.addAttribute("restaurantId", id);
        return prefix() + "/add";
    }

    @Override
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Category entity,
                         BindingResult result, Model model) {
        getCheckAccess().checkAccessCategory(getUser(), id);
        if (result.hasErrors()) {
            entity.setId(id);
            model.addAttribute("restaurantId", entity.getRestaurantId());
            model.addAttribute("entity", entity);
            return prefix() + "/update";
        }
        Optional<Category> category = repository().findById(entity.getId());
        if (category.isPresent()) {
            entity.setLogo(category.get().getLogo());
        }
        try {
            repository().save(entity);
        } catch (Exception e) {
            entity.setId(id);
            model.addAttribute("restaurantId", getRestaurant(id));
            model.addAttribute("entity", entity);
            result.addError(new ObjectError("error", "Ошибка сохранения. Такой элемент уже существует"));
            return prefix() + "/update";
        }
        if (getHttpSession().getAttribute("back") == null) {
            model.addAttribute("list", repository().findAll());
        }
        return getHttpSession().getAttribute("back") != null ? "redirect:" + getHttpSession().getAttribute("back") : prefix() + "/list";
    }

    private long getRestaurant(Long id) {
        Long restaurant = (Long) getHttpSession().getAttribute("restaurant");
        if (restaurant == null) {
            restaurant = repository().findById(id).get().getRestaurantId();
            getHttpSession().setAttribute("restaurant", restaurant);
        }

        return restaurant;
    }

    @Override
    @PostMapping("/add")
    public String add(@Valid Category entity, @Valid MultipartFile file, BindingResult result, Model model) throws IOException, NoSuchAlgorithmException {
        getCheckAccess().checkAccessRestaurant(getUser(), entity.getRestaurantId());
        return super.add(entity, file, result, model);
    }

    @Override
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) throws Throwable {
        getCheckAccess().checkAccessCategory(getUser(), id);
        return super.showUpdateForm(id, model);
    }

    @Override
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) throws Throwable {
        getCheckAccess().checkAccessCategory(getUser(), id);
        return super.delete(id, model);
    }
}
