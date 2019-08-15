package com.restaurant.controller;

import com.restaurant.entity.Category;
import com.restaurant.entity.Dish;
import com.restaurant.entity.Event;
import com.restaurant.entity.Icon;
import com.restaurant.entity.Photo;
import com.restaurant.entity.Restaurant;
import com.restaurant.entity.SubCategory;
import com.restaurant.service.CategoryService;
import com.restaurant.service.DishService;
import com.restaurant.service.EventService;
import com.restaurant.service.IconService;
import com.restaurant.service.PhotoService;
import com.restaurant.service.RestaurantService;
import com.restaurant.service.SubCategoryService;
import com.restaurant.utils.ResizeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import static com.restaurant.utils.ManageFiles.createIcon;
import static com.restaurant.utils.ManageFiles.createPhoto;
import static com.restaurant.utils.ManageFiles.deleteFile;
import static com.restaurant.utils.ManageFiles.saveFile;

@Controller
public class FileUploadController {

    public static final String FAILED_UPLOAD_MESSAGE = "You failed to upload [%s] because the file because %s";

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    PhotoService photoService;

    @Autowired
    IconService iconService;

    @Autowired
    DishService dishService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    SubCategoryService subCategoryService;

    @Autowired
    EventService eventService;

    @PostMapping(value = "/restaurants/upload/{id}")
    public String uploadRestaurantHandler(@RequestParam("file") MultipartFile file,
                                          @PathVariable("id") long id,
                                          Model model) throws IOException, NoSuchAlgorithmException {
        if (file.isEmpty()) {
            model.addAttribute("message", String.format(FAILED_UPLOAD_MESSAGE, file.getName(), "file is empty"));
        } else {
            Restaurant restaurant = restaurantService.findById(id).get();
            restaurant.getPhotos().add(createPhoto(saveFile(file, ResizeImage.Size.PHOTO)));
            restaurantService.save(restaurant);
        }

        return "redirect:/restaurants/edit/" + id;
    }

    @PostMapping(value = "/restaurants/upload/{id}/action")
    public String uploadRestaurantActionHandler(@RequestParam("file") MultipartFile file,
                                          @PathVariable("id") long id,
                                          Model model) throws IOException, NoSuchAlgorithmException {
        if (file.isEmpty()) {
            model.addAttribute("message", String.format(FAILED_UPLOAD_MESSAGE, file.getName(), "file is empty"));
        } else {
            Restaurant restaurant = restaurantService.findById(id).get();
            restaurant.getActions().add(createPhoto(saveFile(file, ResizeImage.Size.PHOTO)));
            restaurantService.save(restaurant);
        }

        return "redirect:/restaurants/edit/" + id;
    }

    @PostMapping(value = "/restaurants/upload/{id}/logo")
    public String uploadRestaurantLogoHandler(@RequestParam("file") MultipartFile file,
                                          @PathVariable("id") long id,
                                          Model model) throws IOException, NoSuchAlgorithmException {
        if (file.isEmpty()) {
            model.addAttribute("message", String.format(FAILED_UPLOAD_MESSAGE, file.getName(), "file is empty"));
        } else {
            Restaurant restaurant = restaurantService.findById(id).get();
            restaurant.setLogo(createPhoto(saveFile(file, ResizeImage.Size.LOGO)));
            restaurantService.save(restaurant);
        }

        return "redirect:/restaurants/edit/" + id;
    }

    @PostMapping(value = "/dishes/upload/{id}")
    public String uploadDishHandler(@RequestParam("file") MultipartFile file,
                                     @PathVariable("id") long id,
                                     Model model) throws IOException, NoSuchAlgorithmException {
        if (file.isEmpty()) {
            model.addAttribute("message", String.format(FAILED_UPLOAD_MESSAGE, file.getName(), "file is empty"));
        } else {
            Dish dish = dishService.findById(id).get();
            dish.setLogo(createPhoto(saveFile(file, ResizeImage.Size.PHOTO)));
            dishService.save(dish);
        }

        return "redirect:/dishes/edit/" + id;
    }

    @PostMapping(value = "/categories/upload/{id}")
    public String uploadCategoryHandler(@RequestParam("file") MultipartFile file,
                                     @PathVariable("id") long id,
                                     Model model) throws IOException, NoSuchAlgorithmException {
        if (file.isEmpty()) {
            model.addAttribute("message", String.format(FAILED_UPLOAD_MESSAGE, file.getName(), "file is empty"));
        } else {
            Category category = categoryService.findById(id).get();
            category.setLogo(createPhoto(saveFile(file, ResizeImage.Size.LOGO)));
            categoryService.save(category);
        }

        return "redirect:/categories/edit/" + id;
    }

    @PostMapping(value = "/subcategories/upload/{id}")
    public String uploadSubcategoryHandler(@RequestParam("file") MultipartFile file,
                                     @PathVariable("id") long id,
                                     Model model) throws IOException, NoSuchAlgorithmException {
        if (file.isEmpty()) {
            model.addAttribute("message", String.format(FAILED_UPLOAD_MESSAGE, file.getName(), "file is empty"));
        } else {
            SubCategory subCategory = subCategoryService.findById(id).get();
            subCategory.setLogo(createPhoto(saveFile(file, ResizeImage.Size.LOGO)));
            subCategoryService.save(subCategory);
        }

        return "redirect:/subcategories/edit/" + id;
    }

    @PostMapping(value = "/ingredients/upload/{id}")
    public String uploadIngredientIconHandler(@RequestParam("file") MultipartFile file,
                                     @PathVariable("id") long id,
                                     Model model) throws IOException, NoSuchAlgorithmException {
        if (file.isEmpty()) {
            model.addAttribute("message", String.format(FAILED_UPLOAD_MESSAGE, file.getName(), "file is empty"));
        } else {
            iconService.save(createIcon(saveFile(file, ResizeImage.Size.FULL)));
        }

        return "redirect:/ingredients/edit/" + id;
    }

    @PostMapping(value = "/events/upload/{id}")
    public String uploadEventPhotoHandler(@RequestParam("file") MultipartFile file,
                                              @PathVariable("id") long id,
                                              Model model) throws IOException, NoSuchAlgorithmException {
        if (file.isEmpty()) {
            model.addAttribute("message", String.format(FAILED_UPLOAD_MESSAGE, file.getName(), "file is empty"));
        } else {
            Event event = eventService.findById(id).get();
            event.getPhotos().add(createPhoto(saveFile(file, ResizeImage.Size.PHOTO)));
            eventService.save(event);
        }

        return "redirect:/events/edit/" + id;
    }

    @GetMapping(value = "/restaurants/deleteImage/{restaurantId}/{imageId}" )
    public String deleteRestaurantHandler(@PathVariable("restaurantId") long restaurantId,
                                          @PathVariable("imageId") long imageId,
                                          Model model) throws IOException {
            Restaurant restaurant = restaurantService.findById(restaurantId).get();

            Photo photo = photoService.findById(imageId).get();
            if (restaurant.getLogo() == photo) {
                 restaurant.setLogo(null);
            } else {
                restaurant.getActions().remove(photo);
                restaurant.getPhotos().remove(photo);
            }
            restaurantService.save(restaurant);
            deleteFile(photo.getPath());
            photoService.delete(photo);

        return "redirect:/restaurants/edit/" + restaurantId;
    }

    @GetMapping(value = "/events/deleteImage/{dishId}/{imageId}")
    public String deleteEventHandler(@PathVariable("dishId") long dishId,
                                    @PathVariable("imageId") long imageId,
                                    Model model) throws IOException {

        Event event = eventService.findById(dishId).get();

        Photo photo = photoService.findById(imageId).get();
        event.getPhotos().remove(photo);
        eventService.save(event);
        deleteFile(photo.getPath());
        photoService.delete(photo);

        return "redirect:/events/edit/" + dishId;
    }


    @GetMapping(value = "/dishes/deleteImage/{dishId}/{imageId}")
    public String deleteDishHandler(@PathVariable("dishId") long dishId,
                                          @PathVariable("imageId") long imageId,
                                          Model model) throws IOException {

        Dish dish = dishService.findById(dishId).get();
        Photo photo = photoService.findById(imageId).get();
        dish.setLogo(null);
        dishService.save(dish);
        deleteFile(photo.getPath());
        photoService.delete(photo);

        return "redirect:/dishes/edit/" + dishId;
    }

    @GetMapping(value = "/categories/deleteImage/{categoryId}/{imageId}")
    public String deleteCategoryHandler(@PathVariable("categoryId") long categoryId,
                                          @PathVariable("imageId") long imageId,
                                          Model model) throws IOException {

        Category category = categoryService.findById(categoryId).get();
        Photo photo = photoService.findById(imageId).get();
        category.setLogo(null);
        categoryService.save(category);
        deleteFile(photo.getPath());
        photoService.delete(photo);

        return "redirect:/categories/edit/" + categoryId;
    }

    @GetMapping(value = "/subcategories/deleteImage/{subcategoryId}/{imageId}")
    public String deleteSubCategoryHandler(@PathVariable("subcategoryId") long subcategoryId,
                                          @PathVariable("imageId") long imageId,
                                          Model model) throws IOException {

        SubCategory subCategory = subCategoryService.findById(subcategoryId).get();
        Photo photo = photoService.findById(imageId).get();
        subCategory.setLogo(null);
        subCategoryService.save(subCategory);
        deleteFile(photo.getPath());
        photoService.delete(photo);

        return "redirect:/subcategories/edit/" + subcategoryId;
    }

    @GetMapping(value = "/image/{url}")
    @ResponseBody
    public HttpEntity<byte[]> getImage(@PathVariable(value = "url") String url) throws IOException {

        Photo photo = photoService.getPhotoByUrl(url);
        if(photo == null) {
            return new HttpEntity<>(null, new HttpHeaders());
        }
        byte[] array = Files.readAllBytes(Paths.get(photo.getPath()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(array.length);
        headers.setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS));

        return new HttpEntity<>(array, headers);
    }

    @GetMapping(value = "/icon/{url}")
    @ResponseBody
    public HttpEntity<byte[]> getIcon(@PathVariable(value = "url") String url) throws IOException {

        Icon icon = iconService.getPhotoByUrl(url);
        if(icon == null) {
            return new HttpEntity<>(null, new HttpHeaders());
        }
        byte[] array = Files.readAllBytes(Paths.get(icon.getPath()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(array.length);
        headers.setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS));

        return new HttpEntity<>(array, headers);
    }

}
