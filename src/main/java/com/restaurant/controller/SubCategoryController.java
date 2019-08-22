package com.restaurant.controller;

import com.restaurant.entity.Category;
import com.restaurant.entity.Restaurant;
import com.restaurant.entity.SubCategory;
import com.restaurant.entity.Town;
import com.restaurant.service.CategoryService;
import com.restaurant.service.DishService;
import com.restaurant.service.RestaurantService;
import com.restaurant.service.SubCategoryService;
import com.restaurant.service.TownService;
import com.restaurant.utils.ResizeImage;
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

import static com.restaurant.utils.ManageFiles.createPhoto;
import static com.restaurant.utils.ManageFiles.saveFile;

@Controller
@RequestMapping("/subcategories")
public class SubCategoryController extends AbstractController<SubCategoryService, SubCategory>{

    @Autowired
    SubCategoryService subCategoryService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    DishService dishService;

    @Autowired
    TownService townService;

    @Override
    SubCategoryService repository() {
        return subCategoryService;
    }

    @Override
    String prefix() {
        return "/subcategories";
    }

    @Override
    @GetMapping("/add")
    public String showSignUpForm(SubCategory entity, @PathParam(value ="id") long id, Model model) {
        getCheckAccess().checkAccessCategory(getUser(), id);
        entity.setCategoryId(id);
        entity.setActive(true);
        model.addAttribute("restaurantId", getHttpSession().getAttribute("restaurant"));
        model.addAttribute("subcategory", entity);
        Optional<Category> category = categoryService.findById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
        }
        return prefix() + "/add";
    }

    @Override
    @PostMapping("/add")
    public String add(@Valid SubCategory entity, @Valid MultipartFile file, BindingResult result, Model model) throws IOException, NoSuchAlgorithmException {
        getCheckAccess().checkAccessCategory(getUser(), entity.getCategoryId());
        if (result.hasErrors() ) {
            model.addAttribute("restaurantId", (Long) getHttpSession().getAttribute("restaurant"));
            model.addAttribute("subcategory", entity);
            model.addAttribute("categories", categoryService.findByRestaurant((Long) getHttpSession().getAttribute("restaurant")));
            return prefix() + "/add";
        }
        if (!file.isEmpty()) {
            entity.setLogo(createPhoto(saveFile(file, ResizeImage.Size.LOGO)));
        }
        try {
            repository().save(entity);
        } catch (Exception e) {
            result.addError(new ObjectError("Error", "Такой элемент уже существует"));
            model.addAttribute("restaurantId", (Long) getHttpSession().getAttribute("restaurant"));
            model.addAttribute("subcategory", entity);
            model.addAttribute("categories", categoryService.findByRestaurant((Long) getHttpSession().getAttribute("restaurant")));
            return prefix() + "/add";
        }
        return getHttpSession().getAttribute("back") != null ? "redirect:" + getHttpSession().getAttribute("back") : prefix() + "/list";
    }

    @Override
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) throws Throwable {
        SubCategory subCategory = repository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        getCheckAccess().checkAccessCategory(getUser(), subCategory.getCategoryId());
        model.addAttribute("restaurantId", getRestaurant(id));
        model.addAttribute("categories", categoryService.findByRestaurant((Long) getHttpSession().getAttribute("restaurant")));
        model.addAttribute("subcategory", subCategory);
        return  prefix() + "/update";
    }

    @Override
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid SubCategory entity,
                         BindingResult result, Model model) {
        getCheckAccess().checkAccessCategory(getUser(), entity.getCategoryId());
        if (result.hasErrors()) {
            entity.setId(id);
            model.addAttribute("restaurantId", getRestaurant(id));
            model.addAttribute("categories", categoryService.findByRestaurant((Long) getHttpSession().getAttribute("restaurant")));
            model.addAttribute("entity", entity);
            return prefix() + "/update";
        }
        Optional<SubCategory> subCategory = repository().findById(entity.getId());
        if (subCategory.isPresent()) {
            entity.setLogo(subCategory.get().getLogo());
        }
        try {
            repository().save(entity);
        } catch (Exception e) {
            entity.setId(id);
            model.addAttribute("restaurantId", getHttpSession().getAttribute("restaurant"));
            model.addAttribute("entity", entity);
            model.addAttribute("categories", categoryService.findByRestaurant((Long) getHttpSession().getAttribute("restaurant")));
            result.addError(new ObjectError("error", "Ошибка сохранения. Такой элемент уже существует"));
            return prefix() + "/update";
        }
        if(getHttpSession().getAttribute("back") == null) {
            model.addAttribute("list", repository().findAll());
        }
        return getHttpSession().getAttribute("back") != null ? "redirect:" + getHttpSession().getAttribute("back") : prefix() + "/list";
    }

    @GetMapping("/list/{id}")
    public String index(Model model, @PathVariable(name="id") long id ) {
        getCheckAccess().checkAccessCategory(getUser(), id);
        Restaurant restaurant = restaurantService.findById((Long) getHttpSession().getAttribute("restaurant"))
                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant Id:" + getHttpSession().getAttribute("restaurant")));

        Optional<Category> category = categoryService.findById(id);
        model.addAttribute("restaurant", restaurant);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            model.addAttribute("subcategories", subCategoryService.findByCategoryId(category.get().getId()));
            getHttpSession().setAttribute("category", category.get());
        }
        Optional<Town> town = townService.findById(restaurant.getCity());
        if (town.isPresent()) {
            model.addAttribute("town", town.get());
        }
        model.addAttribute("restaurantId", getHttpSession().getAttribute("restaurant"));

        getHttpSession().setAttribute("restaurantName", restaurant.getName());
        getHttpSession().setAttribute("back", prefix() + "/list/" + id);

        return prefix() + "/list";
    }

    @Override
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) throws Throwable {
        getCheckAccess().checkAccessSubCategory(getUser(), id);
        return super.delete(id, model);
    }

    @Override
    @GetMapping("/list")
    public String index(Model model) {

        return "redirect:" + getHttpSession().getAttribute("back");
    }

    private long getRestaurant(Long id) {
        Long restaurant = (Long) getHttpSession().getAttribute("restaurant");
        if (restaurant == null) {
            long categoryId = subCategoryService.findById(id).get().getCategoryId();
            restaurant = categoryService.findById(categoryId).get().getRestaurantId();
            getHttpSession().setAttribute("restaurant", restaurant);
        }

        return restaurant;
    }

}
