package com.restaurant.controller;

import com.restaurant.entity.Category;
import com.restaurant.entity.Dish;
import com.restaurant.entity.SubCategory;
import com.restaurant.service.AllergenService;
import com.restaurant.service.CategoryService;
import com.restaurant.service.DishService;
import com.restaurant.service.IngredientService;
import com.restaurant.service.ProteinService;
import com.restaurant.service.RestaurantService;
import com.restaurant.service.SubCategoryService;
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
import java.util.ArrayList;
import java.util.List;

import static com.restaurant.utils.ManageFiles.createPhoto;
import static com.restaurant.utils.ManageFiles.saveFile;

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
        getCheckAccess().checkAccessSubCategory(getUser(), id);
        dish.setSubCategoryId(id);
        dish.setActive(true);
        model.addAttribute("subcategory", subCategoryService.findById(id).get());
        model.addAttribute("ingredients", ingredientService.findAll());
        model.addAttribute(RESTAURANTID_STRING, getHttpSession().getAttribute(RESTAURANT_STRING));
        model.addAttribute("allergens", allergenService.findAll());
        model.addAttribute("proteins", proteinService.findAll());
        model.addAttribute("linkEnable", restaurantService.findById((Long) getHttpSession().getAttribute(RESTAURANT_STRING)).get().isVideoLink());
        return prefix() + "/add";
    }

    @Override
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) throws Throwable {
        Dish entity = repository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        getCheckAccess().checkAccessSubCategory(getUser(), entity.getSubCategoryId());

        model.addAttribute(RESTAURANTID_STRING, getHttpSession().getAttribute(RESTAURANT_STRING));
        model.addAttribute(ENTITY_STRING, entity);
        List<SubCategory> subCategories = new ArrayList<>();
        categoryService.findByRestaurant((Long) getHttpSession().getAttribute(RESTAURANT_STRING)).forEach(c -> subCategories.addAll(subCategoryService.findByCategoryId(c.getId())));
        model.addAttribute("linkEnable", restaurantService.findById((Long) getHttpSession().getAttribute(RESTAURANT_STRING)).get().isVideoLink());
        model.addAttribute("subcategories", subCategories);
        model.addAttribute("ingredients", entity.getIngredients());
        model.addAttribute("allergens", allergenService.findAll());
        model.addAttribute("proteins", proteinService.findAll());

        return prefix() + UPDATE_STRING;
    }

    @Override
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Dish entity,
                             BindingResult result, Model model) {
        getCheckAccess().checkAccessSubCategory(getUser(), entity.getSubCategoryId());
        Long restaurantId = (Long) getHttpSession().getAttribute(RESTAURANT_STRING);
        SubCategory subCategory = subCategoryService.findById(entity.getSubCategoryId()).get();
        if (restaurantId == null) {
            Category category = categoryService.findById(subCategory.getCategoryId()).get();
            restaurantId = category.getRestaurantId();
            getHttpSession().setAttribute(RESTAURANT_STRING, restaurantId);
        }

        if (result.hasErrors()) {
            entity.setId(id);
            model.addAttribute(RESTAURANTID_STRING, restaurantId);
            model.addAttribute(ENTITY_STRING, entity);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("ingredients", ingredientService.findAll());
            return prefix() + UPDATE_STRING;
        }
        entity.setLogo(dishService.findById(entity.getId()).get().getLogo());
        if (entity.getVideo() != null && entity.getVideo().isEmpty()) {
            entity.setVideo(null);
        }
        try {
            repository().save(entity);
        } catch (Exception e) {
            entity.setId(id);
            model.addAttribute(RESTAURANTID_STRING, restaurantId);
            model.addAttribute(ENTITY_STRING, entity);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("ingredients", ingredientService.findAll());
            result.addError(new ObjectError("error", "Ошибка сохранения. Такой элемент уже существует"));
            return prefix() + UPDATE_STRING;
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
        getCheckAccess().checkAccessSubCategory(getUser(), id);
        Long restaurantId = (Long) getHttpSession().getAttribute(RESTAURANT_STRING);
        SubCategory subCategory = subCategoryService.findById(id).get();
        if (restaurantId == null) {
            Category category = categoryService.findById(subCategory.getCategoryId()).get();
            restaurantId = category.getRestaurantId();
        }
        String restaurantName = (String) getHttpSession().getAttribute("restaurantName");
        if (restaurantName == null) {
            restaurantName = restaurantService.findById(restaurantId).get().getName();
        }
        model.addAttribute(RESTAURANTID_STRING, restaurantId);
        model.addAttribute("restaurantName", restaurantName);

        model.addAttribute("subcategory", subCategoryService.findById(id).get());
        model.addAttribute("dishes", repository().findBySubCategoryId(id));
        return prefix() + "/list";
    }

    @Override
    @PostMapping("/add")
    public String add(@Valid Dish entity, @Valid MultipartFile file, BindingResult result, Model model) throws IOException, NoSuchAlgorithmException {
        getCheckAccess().checkAccessSubCategory(getUser(), entity.getSubCategoryId());
        if (result.hasErrors()) {
            List<Category> categories = categoryService.findByRestaurant((Long) getHttpSession().getAttribute(RESTAURANT_STRING));
            List<SubCategory> subCategories = new ArrayList<>();
            categories.forEach(c -> subCategories.addAll(subCategoryService.findByCategoryId(c.getId())));

            model.addAttribute("subcategories", subCategories);
            model.addAttribute("categories", categories);
            model.addAttribute("ingredients", ingredientService.findAll());
            model.addAttribute("allergens", allergenService.findAll());
            model.addAttribute("proteins", proteinService.findAll());
            model.addAttribute(RESTAURANTID_STRING, (Long) getHttpSession().getAttribute(RESTAURANT_STRING));
            return prefix() + "/add";
        }
        if (!file.isEmpty()) {
            entity.setLogo(createPhoto(saveFile(file, ResizeImage.Size.PHOTO)));
        }
        if (entity.getVideo() != null && entity.getVideo().isEmpty()) {
            entity.setVideo(null);
        }
        try {
            entity = repository().save(entity);
        } catch (Exception e) {
            result.addError(new ObjectError("Error", "Такой элемент уже существует"));
            List<Category> categories = categoryService.findByRestaurant((Long) getHttpSession().getAttribute(RESTAURANT_STRING));
            List<SubCategory> subCategories = new ArrayList<>();
            categories.forEach(c -> subCategories.addAll(subCategoryService.findByCategoryId(c.getId())));

            model.addAttribute("subcategories", subCategories);
            model.addAttribute("categories", categories);
            model.addAttribute("ingredients", ingredientService.findAll());
            model.addAttribute("allergens", allergenService.findAll());
            model.addAttribute("proteins", proteinService.findAll());
            model.addAttribute(RESTAURANTID_STRING, (Long) getHttpSession().getAttribute(RESTAURANT_STRING));
            return prefix() + "/add";
        }
        if (getHttpSession().getAttribute("back") == null) {
            model.addAttribute("list", repository().findAll());
        }
        return  "redirect:" + prefix() + "/list/" + entity.getSubCategoryId();
    }

    @SuppressWarnings("unchecked")
    @Override
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) throws Throwable {
        Dish entity =  repository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid  Id:" + id));
        getCheckAccess().checkAccessSubCategory(getUser(), entity.getSubCategoryId());
        repository().delete(entity);
        if (getHttpSession().getAttribute("back") == null) {
            model.addAttribute("list", repository().findAll());
        }
        return "redirect:" + prefix() + "/list/" + entity.getSubCategoryId();
    }

}
