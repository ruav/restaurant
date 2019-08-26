package com.restaurant.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.restaurant.entity.Allergen;
import com.restaurant.entity.Category;
import com.restaurant.entity.Dish;
import com.restaurant.entity.Hostess;
import com.restaurant.entity.Ingredient;
import com.restaurant.entity.Photo;
import com.restaurant.entity.Protein;
import com.restaurant.entity.SubCategory;
import com.restaurant.service.IconService;
import com.restaurant.utils.DtoConverter;
import com.restaurant.utils.ResizeImage;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.restaurant.utils.ManageFiles.createIcon;
import static com.restaurant.utils.ManageFiles.createPhoto;
import static com.restaurant.utils.ManageFiles.saveFile;

@RestController
@RequestMapping("/web")
@Api(value = "MobileEndpoint")
public class WebEndpoint extends AbstractRemoteController {

    @Autowired
    IconService iconService;

    @PostMapping(value = "/upload/hostes")
    public String uploadRestaurantHandler(@RequestParam("file") MultipartFile file,
                                          @PathVariable("id") long id,
                                          HttpServletRequest request) throws IOException, NoSuchAlgorithmException {

        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            return "Unauthorizated";
        }

        if (file.isEmpty()) {
            return  "file is empty";
        } else {
            Optional<Hostess> hostes = hostessService.findById(id);
            if (hostes.isPresent()) {
                Photo photo = createPhoto(saveFile(file, ResizeImage.Size.PHOTO));
                hostes.get().setPhoto(photo);
                hostessService.save(hostes.get());
                return photo.getUrl();
            }
        }
        return "";
    }

    @PostMapping("/create/category")
    public long createCategory(@RequestParam boolean active,
                               @RequestParam String name,
                               @RequestParam MultipartFile file,
                               HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Category category = new Category();
        if (!file.isEmpty()) {
            category.setLogo(createPhoto(saveFile(file, ResizeImage.Size.LOGO)));
        }
        category.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        category.setActive(active);
        category.setName(name);
        return categoryService.save(category).getId();
    }

    @GetMapping("/get/category")
    public String getCategory(@RequestParam long id,
                               HttpServletRequest request) throws JsonProcessingException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            return "Access denied";
        }

        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) return "Not found";
        List<SubCategory> subCategories = subCategoryService.findActiveByCategoryId(category.get().getId());

        return mapper.writeValueAsString(DtoConverter.getCategoryDto(category.get(),
                subCategories,
                subCategories.stream().collect(Collectors.toMap(SubCategory::getId, subCategory -> dishService.findActiveBySubCategoryId(subCategory.getId()), (a, b) -> b)),
                getUrl(request)));
    }

    @PostMapping("/update/category")
    public long updateCategory(@RequestParam long id,
                               @RequestParam boolean active,
                               @RequestParam(required = false) MultipartFile file,
                               @RequestParam String name,
                               HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) return -1l;
        if (!file.isEmpty()) {
            category.get().setLogo(createPhoto(saveFile(file, ResizeImage.Size.LOGO)));
        }
        category.get().setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        category.get().setActive(active);
        category.get().setName(name);
        categoryService.save(category.get());
        return id;
    }

    @PostMapping("/delete/category")
    public long deleteCategory(@RequestParam long id,
                               HttpServletRequest request) {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) return -1l;
        categoryService.delete(category.get());
        return id;
    }

    @PostMapping("/create/subcategory")
    public long createSubcategory(@RequestParam boolean active,
                                  @RequestParam String name,
                                  @RequestParam long categoryId,
                                  @RequestParam(required = false) MultipartFile file,
                                  HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        SubCategory subCategory = new SubCategory();
        subCategory.setName(name);
        subCategory.setCategoryId(categoryId);
        subCategory.setActive(active);
        if (!file.isEmpty()) {
            subCategory.setLogo(createPhoto(saveFile(file, ResizeImage.Size.LOGO)));
        }
        return subCategoryService.save(subCategory).getId();
    }

    @GetMapping("/get/subcategory")
    public String getSubcategory(@RequestParam long id,
                              HttpServletRequest request) throws JsonProcessingException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            return "Access denied";
        }

        Optional<SubCategory> subCategory = subCategoryService.findById(id);
        if (!subCategory.isPresent()) return "Not found";

        return mapper.writeValueAsString(DtoConverter.getSubCategoryDto(subCategory.get(), dishService.findActiveBySubCategoryId(subCategory.get().getId()),
                getUrl(request)));
    }

    @PostMapping("/update/subcategory")
    public long updateSubcategory(@RequestParam long id,
                               @RequestParam boolean active,
                               @RequestParam String name,
                               @RequestParam long categoryId,
                               @RequestParam(required = false) MultipartFile file,
                               HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Optional<SubCategory> subCategory = subCategoryService.findById(id);
        if (!subCategory.isPresent()) return -1l;
        subCategory.get().setCategoryId(categoryId);
        subCategory.get().setActive(active);
        subCategory.get().setName(name);
        if (!file.isEmpty()) {
            subCategory.get().setLogo(createPhoto(saveFile(file, ResizeImage.Size.LOGO)));
        }
        subCategoryService.save(subCategory.get());
        return id;
    }

    @PostMapping("/delete/subcategory")
    public long deleteSubcategory(@RequestParam long id,
                               HttpServletRequest request) {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Optional<SubCategory> subCategory = subCategoryService.findById(id);
        if (!subCategory.isPresent()) return -1l;
        subCategoryService.delete(subCategory.get());
        return id;
    }


    @PostMapping("/create/dish")
    public long createDish(@RequestParam boolean active,
                                  @RequestParam String name,
                                  @RequestParam String description,
                                  @RequestParam String video,
                                  @RequestParam long subCategoryId,
                                  @RequestParam int calories,
                                  @RequestParam int carbohydrates,
                                  @RequestParam int fats,
                                  @RequestParam int fiber,
                                  @RequestParam float price,
                                  @RequestParam int salt,
                                  @RequestParam int cellulose,
                                  @RequestParam int sugar,
                                  @RequestParam int saturatedFats,
                                  @RequestParam int weight,
                                  @RequestParam List<Long> proteins,
                                  @RequestParam List<Long> ingredients,
                                  @RequestParam List<Long> allergens,
                                  @RequestParam(required = false) MultipartFile file,
                                  HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Dish dish = new Dish();
        dish.setSubCategoryId(subCategoryId);
        dish.setVideo(video);
        dish.setActive(active);
        dish.setCalories(calories);
        dish.setCarbohydrates(carbohydrates);
        dish.setCellulose(cellulose);
        dish.setDescription(description);
        dish.setFats(fats);
        dish.setFiber(fiber);
        dish.setName(name);
        dish.setPrice(price);
        dish.setSalt(salt);
        dish.setSugar(sugar);
        dish.setSaturatedFats(saturatedFats);
        dish.setWeight(weight);

        Set<Protein> proteinList = new HashSet<>();
        Set<Ingredient> ingredientList = new HashSet<>();
        Set<Allergen> allergenList = new HashSet<>();
        proteins.stream().map(protein -> proteinService.findById(protein)).forEach(optional -> optional.ifPresent(proteinList::add));
        ingredients.stream().map(ingredient -> ingredientService.findById(ingredient)).forEach(optional -> optional.ifPresent(ingredientList::add));
        allergens.stream().map(allergen -> allergenService.findById(allergen)).forEach(optional -> optional.ifPresent(allergenList::add));

        dish.setProteins(proteinList);
        dish.setIngredients(ingredientList);
        dish.setAllergens(allergenList);

        if (!file.isEmpty()) {
            dish.setLogo(createPhoto(saveFile(file, ResizeImage.Size.LOGO)));
        }
        return dishService.save(dish).getId();
    }

    @GetMapping("/get/dish")
    public String getDish(@RequestParam long id,
                                 HttpServletRequest request) throws JsonProcessingException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            return "Access denied";
        }

        Optional<Dish> dish = dishService.findById(id);
        if (!dish.isPresent()) return "Not found";

        return mapper.writeValueAsString(DtoConverter.getDishDto(dish.get(), getUrl(request)));
    }

    @PostMapping("/update/dish")
    public long updateDish(@RequestParam long id,
                                  @RequestParam boolean active,
                                  @RequestParam String name,
                                  @RequestParam String description,
                                  @RequestParam String video,
                                  @RequestParam long subCategoryId,
                                  @RequestParam int calories,
                                  @RequestParam int carbohydrates,
                                  @RequestParam int fats,
                                  @RequestParam int fiber,
                                  @RequestParam float price,
                                  @RequestParam int salt,
                                  @RequestParam int cellulose,
                                  @RequestParam int sugar,
                                  @RequestParam int saturatedFats,
                                  @RequestParam int weight,
                                  @RequestParam List<Long> proteins,
                                  @RequestParam List<Long> ingredients,
                                  @RequestParam List<Long> allergens,
                                  @RequestParam(required = false) MultipartFile file,
                                  HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Optional<Dish> dish = dishService.findById(id);
        if (!dish.isPresent()) return -1l;
        dish.get().setSubCategoryId(subCategoryId);
        dish.get().setVideo(video);
        dish.get().setActive(active);
        dish.get().setCalories(calories);
        dish.get().setCarbohydrates(carbohydrates);
        dish.get().setCellulose(cellulose);
        dish.get().setDescription(description);
        dish.get().setFats(fats);
        dish.get().setFiber(fiber);
        dish.get().setName(name);
        dish.get().setPrice(price);
        dish.get().setSalt(salt);
        dish.get().setSugar(sugar);
        dish.get().setSaturatedFats(saturatedFats);
        dish.get().setWeight(weight);

        Set<Protein> proteinList = new HashSet<>();
        Set<Ingredient> ingredientList = new HashSet<>();
        Set<Allergen> allergenList = new HashSet<>();
        proteins.stream().map(protein -> proteinService.findById(protein)).forEach(optional -> optional.ifPresent(proteinList::add));
        ingredients.stream().map(ingredient -> ingredientService.findById(ingredient)).forEach(optional -> optional.ifPresent(ingredientList::add));
        allergens.stream().map(allergen -> allergenService.findById(allergen)).forEach(optional -> optional.ifPresent(allergenList::add));

        dish.get().setProteins(proteinList);
        dish.get().setIngredients(ingredientList);
        dish.get().setAllergens(allergenList);

        if (!file.isEmpty()) {
            dish.get().setLogo(createPhoto(saveFile(file, ResizeImage.Size.LOGO)));
        }
        dishService.save(dish.get()).getId();
        return id;
    }

    @PostMapping("/delete/dish")
    public long deleteDish(@RequestParam long id,
                                  HttpServletRequest request) {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Optional<Dish> dish = dishService.findById(id);
        if (!dish.isPresent()) return -1l;
        dishService.delete(dish.get());
        return id;
    }

    @PostMapping("/create/ingredient")
    public long createIngredient(@RequestParam String name,
                               @RequestParam(required = false) MultipartFile file,
                               @RequestParam(required = false) Long iconId,
                               HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        if (!file.isEmpty()) {
            ingredient.setLogo(iconService.save(createIcon(saveFile(file, ResizeImage.Size.FULL))));
        } else if (iconId != null) {
            ingredient.setLogo(iconService.findById(iconId).get());
        }

        return ingredientService.save(ingredient).getId();
    }

    @PostMapping("/update/ingredient")
    public long updateCategory(@RequestParam long id,
                               @RequestParam String name,
                               @RequestParam(required = false) MultipartFile file,
                               @RequestParam(required = false) Long iconId,
                               HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Optional<Ingredient> ingredient = ingredientService.findById(id);
        if (!ingredient.isPresent()) return -1l;
        if (!file.isEmpty()) {
            ingredient.get().setLogo(iconService.save(createIcon(saveFile(file, ResizeImage.Size.FULL))));
        } else if (iconId != null) {
            ingredient.get().setLogo(iconService.findById(iconId).get());
        }
        ingredientService.save(ingredient.get());
        return id;
    }

}
