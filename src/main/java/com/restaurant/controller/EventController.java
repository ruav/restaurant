package com.restaurant.controller;

import com.restaurant.entity.Category;
import com.restaurant.entity.Event;
import com.restaurant.entity.SubCategory;
import com.restaurant.service.EventService;
import com.restaurant.service.EventTypeService;
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

@Controller
@RequestMapping("/events")
public class EventController extends AbstractController<EventService, Event> {

    @Autowired
    EventService eventService;

    @Override
    String prefix() {
        return "/events";
    }

    @Override
    EventService repository() {
        return eventService;
    }

    @Autowired
    EventTypeService eventTypeService;


    @Override
    @GetMapping("/add")
    public String showSignUpForm(Event event, @PathParam("id") long id, Model model) {
        Long restaurantId = (Long) getHttpSession().getAttribute("restaurant");
        event.setRestaurantId(restaurantId);
        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("event", event);

        return prefix() + "/add";
    }

    @Override
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) throws Throwable {
        Event entity = repository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        model.addAttribute("restaurantId", entity.getRestaurantId());

        model.addAttribute("event", entity);
        model.addAttribute("types", eventTypeService.findAll());

        return prefix() + "/update";
    }

    @Override
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Event entity,
                             BindingResult result, Model model) {
        Long restaurantId = (Long) getHttpSession().getAttribute("restaurant");
        if (restaurantId == null) {
            restaurantId = entity.getRestaurantId();
            getHttpSession().setAttribute("restaurant", restaurantId);
        }

        if (result.hasErrors()) {
            entity.setId(id);
            model.addAttribute("event", entity);
            model.addAttribute("types", eventTypeService.findAll());
            return prefix() + "/update";
        }
        entity.setPhotos(eventService.findById(entity.getId()).get().getPhotos());
        try {
            repository().save(entity);
        } catch (Exception e) {
            entity.setId(id);
            model.addAttribute("event", entity);
            model.addAttribute("types", eventTypeService.findAll());
            result.addError(new ObjectError("error", "Ошибка сохранения. Такой элемент уже существует"));
            return prefix() + "/update";
        }
        if(getHttpSession().getAttribute("back") == null) {
            model.addAttribute("list", repository().findAll());
        }
        return  "redirect:" + prefix() + "/list/" + entity.getRestaurantId();
    }

    @GetMapping("/list/{id}")
    public String index(@PathVariable("id") long id, Model model) {

//        Long restaurantId = (Long) getHttpSession().getAttribute("restaurant");
        model.addAttribute("restaurantId", id);

        model.addAttribute("list", eventService.findByRestaurantId(id));
        return prefix() + "/list";
    }

    @Override
    @PostMapping("/add")
    public String add(@Valid Event entity, @Valid MultipartFile file, BindingResult result, Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("event", entity);
            return prefix() + "/add";
        }
        entity.setRestaurantId((Long) getHttpSession().getAttribute("restaurant"));
        try {
            entity = repository().save(entity);
        } catch (Exception e) {
            result.addError(new ObjectError("Error", "Такой элемент уже существует"));
            model.addAttribute("event", entity);
            return prefix() + "/add";
        }
        if (getHttpSession().getAttribute("back") == null) {
            model.addAttribute("list", repository().findAll());
        }
//        return getHttpSession().getAttribute("back") != null ? "redirect:" + getHttpSession().getAttribute("back") : "redirect:" + prefix() + "/list/" + entity.getSubCategoryId();
        return  "redirect:" + prefix() + "/list/" + entity.getRestaurantId();
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) throws Throwable {
        Event entity =  repository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid  Id:" + id));
        repository().delete(entity);
        if (getHttpSession().getAttribute("back") == null) {
            model.addAttribute("list", repository().findAll());
        }
        return "redirect:" + prefix() + "/list/" + entity.getRestaurantId();
    }

}
