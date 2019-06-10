package com.restaurant.controller;

import com.restaurant.Exception.ForbiddenException;
import com.restaurant.entity.Category;
import com.restaurant.entity.Desk;
import com.restaurant.entity.Dish;
import com.restaurant.entity.Hall;
import com.restaurant.entity.Photo;
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
import com.restaurant.utils.ResizeImage;
import com.restaurant.vo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.restaurant.utils.ManageFiles.createPhoto;
import static com.restaurant.utils.ManageFiles.saveFile;

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

    private final static String PREFIX = "/restaurants";

    @GetMapping("/add")
    public String showSignUpForm(Restaurant restaurant, MultipartFile file,  Model model) {
        model.addAttribute("restaurant", restaurant);
        if (file == null) {
            file = new MultipartFile() {
                @Override
                public String getName() {
                    return null;
                }

                @Override
                public String getOriginalFilename() {
                    return null;
                }

                @Override
                public String getContentType() {
                    return null;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public long getSize() {
                    return 0;
                }

                @Override
                public byte[] getBytes() throws IOException {
                    return new byte[0];
                }

                @Override
                public InputStream getInputStream() throws IOException {
                    return null;
                }

                @Override
                public void transferTo(File file) throws IOException, IllegalStateException {

                }
            };
        }
        model.addAttribute("file", file);
        restaurant.setLogo(new Photo());
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
    public String add(@Valid Restaurant restaurant, @Valid MultipartFile file, BindingResult result, Model model) throws IOException, NoSuchAlgorithmException {
        if (result.hasErrors()) {
            return PREFIX + "/add";
        }

        if (!file.isEmpty()) {
            restaurant.setLogo(createPhoto(saveFile(file, ResizeImage.Size.LOGO)));
        }

        restaurant = restaurantService.save(restaurant);

        userService.addRestaurant(userService.findByEmail(getUser().getUsername()), restaurant);

        return "redirect:" + PREFIX + "/list";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        checkAccess(id);
        Restaurant restaurant = restaurantService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant Id:" + id));

        model.addAttribute("towns", townService.findAll());
        model.addAttribute("restaurant", restaurant);
        return PREFIX + "/update";
    }

    @GetMapping("/info/{id}")
    public String showInfoForm(@PathVariable("id") long id, Model model) {
        checkAccess(id);
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
        checkAccess(id);
        if (result.hasErrors()) {
            restaurant.setId(id);
            return PREFIX + "/update";
        }
        restaurant.setLogo(restaurantService.findById(restaurant.getId()).get().getLogo());
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
        checkAccess(id);
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

    private void checkAccess(long id) {
        if (!getUser().getAuthorities().contains(new SimpleGrantedAuthority(Role.Root.name())) && !userService.findByEmail(getUser().getUsername()).getRestaurants().contains(restaurantService.findById(id).get())) {
            throw new ForbiddenException();
        }
    }


}
