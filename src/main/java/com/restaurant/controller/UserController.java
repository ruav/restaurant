package com.restaurant.controller;

import com.restaurant.entity.PasswordResetToken;
import com.restaurant.entity.User;
import com.restaurant.service.CategoryService;
import com.restaurant.service.DishService;
import com.restaurant.service.PasswordResetService;
import com.restaurant.service.PhotoService;
import com.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userRepository;

    @Autowired
    PasswordResetService resetService;

    @Autowired
    PhotoService photoService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    DishService dishService;

    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return prefix() + "/add";
    }

    String prefix() {
        return "/users";
    }

    @GetMapping("/list")
    public String index(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return prefix() + "/list";
    }

    @PostMapping("/add")
    public String add(@Valid User user, BindingResult result, Model model) {
        User user1 = userRepository.findByEmail(user.getEmail());
        if (result.hasErrors()) {
            return prefix() + "/add";
        }
        if (user.getRole() == null) {
            FieldError error = new FieldError("role", "role", "Не выбрана роль пользователя");
            result.addError(error);
            return prefix() + "/add";
        }
        if (user1 != null) {
            FieldError error = new FieldError("email", "email", "Пользователь с таким email уже существует");
            result.addError(error);
            return prefix() + "/add";
        }

        userRepository.add(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:" + prefix() + "/list";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        model.addAttribute("user", user);
        return prefix() + "/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return prefix() + "/update";
        }
        if (user.getRole() == null) {
            FieldError error = new FieldError("role", "role", "Не выбрана роль пользователя");
            result.addError(error);
            return prefix() + "/add";
        }
        userRepository.update(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:" + prefix() + "/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        List<PasswordResetToken> resetTokens = resetService.findByUserId(user.getId());
        resetTokens.forEach(x -> resetService.delete(x));
        user.getRestaurants().forEach(x -> {
            x.getPhotos().forEach(y -> photoService.delete(y));
           categoryService.findByRestaurant(x.getId()).forEach(
                   c -> {
                       dishService.findByCategoryId(c.getId()).forEach(
                               dish -> {
                                   dish.getPhotos().forEach(photo -> photoService.delete(photo));
                                   dishService.delete(dish);
                               }
                       );
                       categoryService.delete(c);
                   }
           );
        });
        userRepository.delete(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:" + prefix() + "/list";
    }

}