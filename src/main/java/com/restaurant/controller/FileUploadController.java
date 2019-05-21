package com.restaurant.controller;

import com.restaurant.entity.Category;
import com.restaurant.entity.Dish;
import com.restaurant.entity.Icon;
import com.restaurant.entity.Photo;
import com.restaurant.entity.Restaurant;
import com.restaurant.service.CategoryService;
import com.restaurant.service.DishService;
import com.restaurant.service.IconService;
import com.restaurant.service.PhotoService;
import com.restaurant.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Controller
public class FileUploadController {


    private static final String FAILED_UPLOAD_MESSAGE = "You failed to upload [%s] because the file because %s";

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

    @PostMapping(value = "/restaurants/upload/{id}")
    public String uploadRestaurantHandler(@RequestParam("file") MultipartFile file,
                                          @PathVariable("id") long id,
                                          Model model) throws IOException {
        if (file.isEmpty()) {
            model.addAttribute("message", String.format(FAILED_UPLOAD_MESSAGE, file.getName(), "file is empty"));
        } else {
            Restaurant restaurant = restaurantService.findById(id).get();
            Photo photo = new Photo();
            photo.setUrl(UUID.randomUUID().toString());
            photo.setImage(file.getBytes());
            restaurant.getPhotos().add(photo);
            restaurantService.save(restaurant);
        }

        return "redirect:/restaurants/edit/" + id;
    }

    @PostMapping(value = "/restaurants/upload/{id}/logo")
    public String uploadRestaurantLogoHandler(@RequestParam("file") MultipartFile file,
                                          @PathVariable("id") long id,
                                          Model model) throws IOException {
        if (file.isEmpty()) {
            model.addAttribute("message", String.format(FAILED_UPLOAD_MESSAGE, file.getName(), "file is empty"));
        } else {
            Restaurant restaurant = restaurantService.findById(id).get();
            Photo photo = new Photo();
            photo.setUrl(UUID.randomUUID().toString());
            photo.setImage(file.getBytes());
            restaurant.setLogo(photo);
            restaurantService.save(restaurant);
        }

        return "redirect:/restaurants/edit/" + id;
    }

    @PostMapping(value = "/dishes/upload/{id}")
    public String uploadDishHandler(@RequestParam("file") MultipartFile file,
                                     @PathVariable("id") long id,
                                     Model model) throws IOException {
        if (file.isEmpty()) {
            model.addAttribute("message", String.format(FAILED_UPLOAD_MESSAGE, file.getName(), "file is empty"));
        } else {
            Dish dish = dishService.findById(id).get();

            Photo photo = new Photo();
            photo.setUrl(UUID.randomUUID().toString());
            photo.setImage(file.getBytes());
            dish.getPhotos().add(photo);
            dishService.save(dish);
        }

        return "redirect:/dishes/edit/" + id;
    }

    @PostMapping(value = "/categories/upload/{id}")
    public String uploadCategoryHandler(@RequestParam("file") MultipartFile file,
                                     @PathVariable("id") long id,
                                     Model model) throws IOException {
        if (file.isEmpty()) {
            model.addAttribute("message", String.format(FAILED_UPLOAD_MESSAGE, file.getName(), "file is empty"));
        } else {
            Category category = categoryService.findById(id).get();

            Photo photo = new Photo();
            photo.setUrl(UUID.randomUUID().toString());
            photo.setImage(file.getBytes());
            category.getPhotos().add(photo);
            categoryService.save(category);
        }

        return "redirect:/categories/edit/" + id;
    }

    @PostMapping(value = "/ingredients/upload/{id}")
    public String uploadIngredientIconHandler(@RequestParam("file") MultipartFile file,
                                     @PathVariable("id") long id,
                                     Model model) throws IOException {
        if (file.isEmpty()) {
            model.addAttribute("message", String.format(FAILED_UPLOAD_MESSAGE, file.getName(), "file is empty"));
        } else {

            Icon icon = new Icon();
            icon.setUrl(UUID.randomUUID().toString());
            icon.setImage(file.getBytes());
            iconService.save(icon);


        }

        return "redirect:/ingredients/edit/" + id;
    }


    @GetMapping(value = "/restaurants/deleteImage/{restaurantId}/{imageId}" )
    public String deleteRestaurantHandler(@PathVariable("restaurantId") long restaurantId,
                                          @PathVariable("imageId") long imageId,
                                          Model model) {
            Restaurant restaurant = restaurantService.findById(restaurantId).get();

            Photo photo = photoService.findById(imageId).get();
            if (restaurant.getLogo() == photo) {
                 restaurant.setLogo(null);
            } else {
                restaurant.getPhotos().remove(photo);
            }
            restaurantService.save(restaurant);
            photoService.delete(photo);

        return "redirect:/restaurants/edit/" + restaurantId;
    }

    @GetMapping(value = "/dishes/deleteImage/{dishId}/{imageId}")
    public String deleteDishHandler(@PathVariable("dishId") long dishId,
                                          @PathVariable("imageId") long imageId,
                                          Model model) {

            Dish dish = dishService.findById(dishId).get();

            Photo photo = photoService.findById(imageId).get();
            dish.getPhotos().remove(photo);
            dishService.save(dish);
            photoService.delete(photo);

        return "redirect:/dishes/edit/" + dishId;
    }

    @GetMapping(value = "/categories/deleteImage/{categoryId}/{imageId}")
    public String deleteCategoryHandler(@PathVariable("categoryId") long categoryId,
                                          @PathVariable("imageId") long imageId,
                                          Model model) {

            Category category = categoryService.findById(categoryId).get();
            Photo photo = photoService.findById(imageId).get();
            category.getPhotos().remove(photo);
            categoryService.save(category);
            photoService.delete(photo);

        return "redirect:/categories/edit/" + categoryId;
    }

    @GetMapping(value = "/image/{url}")
    @ResponseBody
    public HttpEntity<byte[]> getImage(@PathVariable(value = "url") String url) {

        Photo photo = photoService.getPhotoByUrl(url);
        if(photo == null) {
            return new HttpEntity<>(null, new HttpHeaders());
        }
        byte[] array = photo.getImage();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(array.length);

        return new HttpEntity<>(array, headers);
    }

    @GetMapping(value = "/icon/{url}")
    @ResponseBody
    public HttpEntity<byte[]> getIcon(@PathVariable(value = "url") String url) {

        Icon icon = iconService.getPhotoByUrl(url);
        if(icon == null) {
            return new HttpEntity<>(null, new HttpHeaders());
        }
        byte[] array = icon.getImage();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(array.length);

        return new HttpEntity<>(array, headers);
    }
}
