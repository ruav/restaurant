package com.restaurant.controller;

import com.restaurant.entity.Category;
import com.restaurant.entity.Dish;
import com.restaurant.entity.Photo;
import com.restaurant.entity.SubCategory;
import com.restaurant.service.AllergenService;
import com.restaurant.service.CategoryService;
import com.restaurant.service.DishService;
import com.restaurant.service.IngredientService;
import com.restaurant.service.ProteinService;
import com.restaurant.service.RestaurantService;
import com.restaurant.service.SubCategoryService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/dishes")
public class DishController extends AbstractController<DishService, Dish> {

    @Autowired
    DishService dishService;

    @Override
    String prefix() {
        return "/dishes";
    }

    @Override
    DishService repository() {
        return dishService;
    }

    @Autowired
    CategoryService categoryService;

    @Autowired
    SubCategoryService subCategoryService;

    @Autowired
    IngredientService ingredientService;

    @Autowired
    ProteinService proteinService;

    @Autowired
    AllergenService allergenService;

    @Autowired
    RestaurantService restaurantService;

    @Override
    @GetMapping("/add")
    public String showSignUpForm(Dish dish, @PathParam("id") long id, Model model) {
        dish.setSubCategoryId(0l);

        List<Category> categories = categoryService.findByRestaurant(id);
        List<SubCategory> subCategories = new ArrayList<>();
        categories.forEach(c -> subCategories.addAll(subCategoryService.findByCategoryId(c.getId())));

        model.addAttribute("subcategories", subCategories);
        model.addAttribute("categories", categories);
        model.addAttribute("ingredients", ingredientService.findAll());
        model.addAttribute("restaurantId", id);
        model.addAttribute("allergens", allergenService.findAll());
        model.addAttribute("proteins", proteinService.findAll());
        model.addAttribute("linkEnable", restaurantService.findById(id).get().isVideoLink());
        return prefix() + "/add";
    }

    @Override
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) throws Throwable {
        Dish entity = repository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));

        model.addAttribute("restaurantId", getHttpSession().getAttribute("restaurant"));
        model.addAttribute("entity", entity);
        List<SubCategory> subCategories = new ArrayList<>();
        categoryService.findByRestaurant((Long) getHttpSession().getAttribute("restaurant")).forEach(c -> subCategories.addAll(subCategoryService.findByCategoryId(c.getId())));

        model.addAttribute("subcategories", subCategories);
        model.addAttribute("ingredients", ingredientService.findAll());
        model.addAttribute("allergens", allergenService.findAll());
        model.addAttribute("proteins", proteinService.findAll());

        return prefix() + "/update";
    }

    @Override
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Dish entity,
                             BindingResult result, Model model) {
        Long restaurantId = (Long) getHttpSession().getAttribute("restaurant");
        SubCategory subCategory = subCategoryService.findById(entity.getSubCategoryId()).get();
        if (restaurantId == null) {
            Category category = categoryService.findById(subCategory.getCategoryId()).get();
            restaurantId = category.getRestaurantId();
            getHttpSession().setAttribute("restaurant", restaurantId);
        }

        if (result.hasErrors()) {
            entity.setId(id);
            model.addAttribute("restaurantId", restaurantId);
            model.addAttribute("entity", entity);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("ingredients", ingredientService.findAll());
            return prefix() + "/update";
        }
        entity.setLogo(dishService.findById(entity.getId()).get().getLogo());
        try {
            repository().save(entity);
        } catch (Exception e) {
            entity.setId(id);
            model.addAttribute("restaurantId", restaurantId);
            model.addAttribute("entity", entity);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("ingredients", ingredientService.findAll());
            result.addError(new ObjectError("error", "Ошибка сохранения. Такой элемент уже существует"));
            return prefix() + "/update";
        }
        if(getHttpSession().getAttribute("back") == null) {
            model.addAttribute("list", repository().findAll());
        }
//        return getHttpSession().getAttribute("back") != null
//                ? "redirect:" + getHttpSession().getAttribute("back")
//                : prefix() + "/list";

        return  "redirect:" + prefix() + "/list/" + entity.getSubCategoryId();
    }

    @GetMapping("/list/{id}")
    public String index(@PathVariable("id") long id, Model model) {

        Long restaurantId = (Long) getHttpSession().getAttribute("restaurant");
        SubCategory subCategory = subCategoryService.findById(id).get();
        if (restaurantId == null) {
            Category category = categoryService.findById(subCategory.getCategoryId()).get();
            restaurantId = category.getRestaurantId();
        }
        String restaurantName = (String) getHttpSession().getAttribute("restaurantName");
        if (restaurantName == null) {
            restaurantName = restaurantService.findById(restaurantId).get().getName();
        }
        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("restaurantName", restaurantName);

        model.addAttribute("subcategory", subCategoryService.findById(id).get());
        model.addAttribute("dishes", repository().findBySubCategoryId(id));
        return prefix() + "/list";
    }

    @Override
    @PostMapping("/add")
    public String add(@Valid Dish entity, @Valid MultipartFile file, BindingResult result, Model model) throws IOException {
        if (result.hasErrors()) {
            List<Category> categories = categoryService.findByRestaurant((Long) getHttpSession().getAttribute("restaurant"));
            List<SubCategory> subCategories = new ArrayList<>();
            categories.forEach(c -> subCategories.addAll(subCategoryService.findByCategoryId(c.getId())));

            model.addAttribute("subcategories", subCategories);
            model.addAttribute("categories", categories);
            model.addAttribute("ingredients", ingredientService.findAll());
            model.addAttribute("allergens", allergenService.findAll());
            model.addAttribute("proteins", proteinService.findAll());
            model.addAttribute("restaurantId", (Long) getHttpSession().getAttribute("restaurant"));
            return prefix() + "/add";
        }
        if (!file.isEmpty()) {
            Photo photo = new Photo();
            photo.setUrl(UUID.randomUUID().toString());
            photo.setImage(file.getBytes());
            entity.setLogo(photo);
        }
        try {
            entity = repository().save(entity);
        } catch (Exception e) {
            result.addError(new ObjectError("Error", "Такой элемент уже существует"));
            List<Category> categories = categoryService.findByRestaurant((Long) getHttpSession().getAttribute("restaurant"));
            List<SubCategory> subCategories = new ArrayList<>();
            categories.forEach(c -> subCategories.addAll(subCategoryService.findByCategoryId(c.getId())));

            model.addAttribute("subcategories", subCategories);
            model.addAttribute("categories", categories);
            model.addAttribute("ingredients", ingredientService.findAll());
            model.addAttribute("allergens", allergenService.findAll());
            model.addAttribute("proteins", proteinService.findAll());
            model.addAttribute("restaurantId", (Long) getHttpSession().getAttribute("restaurant"));
            return prefix() + "/add";
        }
        if (getHttpSession().getAttribute("back") == null) {
            model.addAttribute("list", repository().findAll());
        }
//        return getHttpSession().getAttribute("back") != null ? "redirect:" + getHttpSession().getAttribute("back") : "redirect:" + prefix() + "/list/" + entity.getSubCategoryId();
        return  "redirect:" + prefix() + "/list/" + entity.getSubCategoryId();
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) throws Throwable {
        Dish entity =  repository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid  Id:" + id));
        repository().delete(entity);
        if (getHttpSession().getAttribute("back") == null) {
            model.addAttribute("list", repository().findAll());
        }
        return "redirect:" + prefix() + "/list/" + entity.getSubCategoryId();
    }

}
