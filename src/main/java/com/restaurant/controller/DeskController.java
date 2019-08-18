package com.restaurant.controller;

import com.restaurant.entity.Desk;
import com.restaurant.service.CheckAccess;
import com.restaurant.service.DeskService;
import com.restaurant.service.HallService;
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

@Controller
@RequestMapping("/tables")
public class DeskController extends AbstractController<DeskService, Desk> {

    @Autowired
    DeskService deskService;

    @Autowired
    HallService hallService;

    @Autowired
    private CheckAccess checkAccess;

    @Override
    String prefix() {
        return "/tables";
    }

    @Override
    DeskService repository() {
        return deskService;
    }

    @Override
    @GetMapping("/add")
    public String showSignUpForm(Desk desk, @PathParam("id") long id, Model model) {
        checkAccess.checkAccessRestaurant(getUser(), id);
        model.addAttribute("restaurantId", id);
        model.addAttribute("halls", hallService.findByRestaurantId((Long) getHttpSession().getAttribute("restaurant")));
        return prefix() + "/add";
    }

    @Override
    @PostMapping("/add")
    public String add(@Valid Desk entity, @Valid MultipartFile file, BindingResult result, Model model) {
        checkAccess.checkAccessHall(getUser(), entity.getHall());
        if (result.hasErrors() ) {
            model.addAttribute("restaurantId", getHttpSession().getAttribute("restaurant"));
            model.addAttribute("halls", hallService.findByRestaurantId((Long) getHttpSession().getAttribute("restaurant")));
            model.addAttribute("desk", entity);
            return prefix() + "/add";
        }
        try {
            repository().save(entity);
        } catch (Exception e) {
            model.addAttribute("restaurantId", getHttpSession().getAttribute("restaurant"));
            model.addAttribute("halls", hallService.findByRestaurantId((Long) getHttpSession().getAttribute("restaurant")));
            model.addAttribute("desk", entity);
            result.addError(new ObjectError("Error", "Такой элемент уже существует"));
            return prefix() + "/add";
        }
        if(getHttpSession().getAttribute("back") == null) {
            model.addAttribute("list", repository().findAll());
        }
        return getHttpSession().getAttribute("back") != null ? "redirect:" + getHttpSession().getAttribute("back") : prefix() + "/list";
    }

    @Override
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) throws Throwable {
        Desk desk =  repository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        checkAccess.checkAccessHall(getUser(), desk.getHall());
        model.addAttribute("restaurantId", getHttpSession().getAttribute("restaurant"));
        model.addAttribute("halls", hallService.findByRestaurantId((Long) getHttpSession().getAttribute("restaurant")));
        model.addAttribute("desk", desk);
        return  prefix() + "/update";
    }

    @Override
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Desk entity,
                         BindingResult result, Model model) {
        checkAccess.checkAccessHall(getUser(), entity.getHall());
        if (result.hasErrors()) {
            entity.setId(id);
            model.addAttribute("restaurantId", getHttpSession().getAttribute("restaurant"));
            model.addAttribute("halls", hallService.findByRestaurantId((Long) getHttpSession().getAttribute("restaurant")));
            model.addAttribute("desk", entity);
            return prefix() + "/update";
        }
        try {
            repository().save(entity);
        } catch (Exception e) {
            entity.setId(id);
            model.addAttribute("restaurantId", getHttpSession().getAttribute("restaurant"));
            model.addAttribute("halls", hallService.findByRestaurantId((Long) getHttpSession().getAttribute("restaurant")));
            model.addAttribute("desk", entity);
            result.addError(new ObjectError("error", "Ошибка сохранения. Такой элемент уже существует"));
            return prefix() + "/update";
        }
        if(getHttpSession().getAttribute("back") == null) {
            model.addAttribute("list", repository().findAll());
        }
        return getHttpSession().getAttribute("back") != null ? "redirect:" + getHttpSession().getAttribute("back") : prefix() + "/list";
    }

    @Override
    public String delete(long id, Model model) throws Throwable {
        Desk desk =  repository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        checkAccess.checkAccessHall(getUser(), desk.getHall());
        return super.delete(id, model);
    }
}
