package com.restaurant.controller;

import com.restaurant.entity.Hall;
import com.restaurant.service.HallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.websocket.server.PathParam;

@Controller
@RequestMapping("/halls")
public class HallController extends AbstractController<HallService, Hall> {

    @Autowired
    HallService service;

    @Override
    HallService repository() {
        return service;
    }

    @Override
    String prefix() {
        return "/halls";
    }

    @Override
    @GetMapping("/add")
    public String showSignUpForm(Hall entity, @PathParam(value ="id") long id, Model model) {
        entity.setRestaurantId(id);
        model.addAttribute("restaurantId", id);
        return prefix() + "/add";
    }

}
