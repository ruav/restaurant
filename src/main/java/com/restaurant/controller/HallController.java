package com.restaurant.controller;

import com.restaurant.entity.Hall;
import com.restaurant.service.CheckAccess;
import com.restaurant.service.HallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/halls")
public class HallController extends AbstractController<HallService, Hall> {

    @Autowired
    HallService service;

    @Override
    HallService repository() {
        return service;
    }

    @Autowired
    private CheckAccess checkAccess;

    @Override
    String prefix() {
        return "/halls";
    }

    @Override
    @GetMapping("/add")
    public String showSignUpForm(Hall entity, @PathParam(value ="id") long id, Model model) {
        checkAccess.checkAccessRestaurant(getUser(), id);
        entity.setRestaurantId(id);
        model.addAttribute("restaurantId", id);
        return prefix() + "/add";
    }

    @Override
    public String add(@Valid Hall entity, @Valid MultipartFile file, BindingResult result, Model model) throws IOException, NoSuchAlgorithmException {
        checkAccess.checkAccessHall(getUser(), entity.getId());
        return super.add(entity, file, result, model);
    }

    @Override
    public String showUpdateForm(long id, Model model) throws Throwable {
        checkAccess.checkAccessHall(getUser(), id);
        return super.showUpdateForm(id, model);
    }

    @Override
    public String update(long id, @Valid Hall entity, BindingResult result, Model model) {
        checkAccess.checkAccessHall(getUser(), id);
        return super.update(id, entity, result, model);
    }

    @Override
    public String delete(long id, Model model) throws Throwable {
        checkAccess.checkAccessHall(getUser(), id);
        return super.delete(id, model);
    }
}
