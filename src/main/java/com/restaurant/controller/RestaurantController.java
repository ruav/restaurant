package com.restaurant.controller;

import com.restaurant.entity.Category;
import com.restaurant.entity.Desk;
import com.restaurant.entity.Dish;
import com.restaurant.entity.Hall;
import com.restaurant.entity.Restaurant;
import com.restaurant.entity.SubCategory;
import com.restaurant.entity.User;
import com.restaurant.service.CategoryService;
import com.restaurant.service.DeskService;
import com.restaurant.service.DishService;
import com.restaurant.service.HallService;
import com.restaurant.service.RestaurantService;
import com.restaurant.service.SubCategoryService;
import com.restaurant.service.TownService;
import com.restaurant.service.UserService;
import com.restaurant.vo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    SubCategoryService subCategoryService;

    @Autowired
    DishService dishService;

    @Autowired
    HallService hallService;

    @Autowired
    DeskService deskService;

    @Autowired
    UserService userService;

    @Autowired
    TownService townService;


    @Autowired
    private HttpSession httpSession;

    private final String PREFIX = "/restaurants";

    @GetMapping("/add")
    public String showSignUpForm(Restaurant restaurant, Model model) {

        model.addAttribute("towns", townService.findAll());
        return PREFIX + "/add";
    }

    @GetMapping("/list")
    public String index(Model model) {

        boolean root = getUser().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(Role.Root.toString()));

        if (root) {
            List<Restaurant> list = new ArrayList<>();
            userService.findAll().forEach(u -> list.addAll(u.getRestaurants()));

            Map<Long, String> restaunrantsOwner = new HashMap<>();
            for (User x : userService.findAll()) {
                for (Restaurant r : x.getRestaurants()) {
                    if (list.contains(r)) {
                        restaunrantsOwner.put(r.getId(), x.getEmail());
                    }
                }
            }

            model.addAttribute("list", list);
            model.addAttribute("owners", restaunrantsOwner);

        } else {
            List<Restaurant> list = new ArrayList<>(userService.findByEmail(getUser().getUsername()).getRestaurants());
            model.addAttribute("list", list);
        }
        model.addAttribute("towns", townService.findMapAll());

        httpSession.removeAttribute("back");
        httpSession.removeAttribute("restaurant");

        return PREFIX + "/list";
    }

    @PostMapping("/add")
    public String add(@Valid Restaurant restaurant, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return PREFIX + "/add";
        }

        userService.addRestaurant(userService.findByEmail(getUser().getUsername()), restaurantService.save(restaurant));
        return "redirect:" + PREFIX + "/list";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Restaurant restaurant = restaurantService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant Id:" + id));

        model.addAttribute("towns", townService.findAll());
        model.addAttribute("restaurant", restaurant);
        return PREFIX + "/update";
    }

    @GetMapping("/info/{id}")
    public String showInfoForm(@PathVariable("id") long id, Model model) {
        Restaurant restaurant = restaurantService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant Id:" + id));

        model.addAttribute("restaurant", restaurant);

        Map<Category, List<SubCategory>> categoriesMap = new HashMap<>();
        Map<Long, List<Dish>> subcategoriesMap = new HashMap<>();
        for(Category category : categoryService.findByRestaurant(restaurant.getId())) {
            categoriesMap.put(category, new ArrayList<>());
            for(SubCategory subCategory : subCategoryService.findByCategoryId(category.getId())) {
                categoriesMap.get(category).add(subCategory);
                subcategoriesMap.put(subCategory.getId(), dishService.findBySubCategoryId(subCategory.getId()));
            }
        }

        model.addAttribute("categories", categoriesMap);
        model.addAttribute("subcategories", subcategoriesMap);

        Map<Hall, List<Desk>> hallMap = new HashMap<>();
        for(Hall hall : hallService.findByRestaurantId(id)) {
            hallMap.put(hall, deskService.findByHall(hall.getId()));
        }

        model.addAttribute("halls", hallMap);
        model.addAttribute("towns", townService.findMapAll());
        if (httpSession.getAttribute("category") != null) {
            httpSession.removeAttribute("category");
        }
        httpSession.setAttribute("back", "/restaurants/info/" + id);
        httpSession.setAttribute("restaurant", id);
        httpSession.setAttribute("restaurantName", restaurant.getName());
        return PREFIX + "/info";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Restaurant restaurant,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            restaurant.setId(id);
            return PREFIX + "/update";
        }
        restaurant.setPhotos(restaurantService.findById(restaurant.getId()).get().getPhotos());
        try {
            restaurantService.save(restaurant);
        } catch (Exception e) {
            restaurant.setId(id);
            result.addError(new ObjectError("error", "Ошибка сохранения. Такой элемент уже существует"));
            return PREFIX + "/update";
        }
        restaurantService.save(restaurant);
        return "redirect:" + PREFIX + "/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        Restaurant restaurant = restaurantService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant Id:" + id));
        for (User u : userService.findAll()) {
            for (Restaurant r : u.getRestaurants()) {
                if (r.getId() == restaurant.getId()) {
                    u.getRestaurants().remove(r);
                    break;
                }
            }
        }
        restaurantService.delete(restaurant);
        return "redirect:" + PREFIX + "/list";
    }

    private UserDetails getUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
