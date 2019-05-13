package com.restaurant.controller;

import com.restaurant.entity.Dish;
import com.restaurant.entity.Photo;
import com.restaurant.entity.Restaurant;
import com.restaurant.service.DishService;
import com.restaurant.service.PhotoService;
import com.restaurant.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Controller
public class FileUploadController {


    private static final String FAILED_UPLOAD_MESSAGE = "You failed to upload [%s] because the file because %s";
    private static final String SUCCESS_UPLOAD_MESSAGE = "You successfully uploaded file = [%s]";

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    PhotoService photoService;

    @Autowired
    DishService dishService;

    @RequestMapping(value = "/restaurants/upload/{id}", method = RequestMethod.POST)
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

    @RequestMapping(value = "/dishes/upload/{id}", method = RequestMethod.POST)
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


    @RequestMapping(value = "/restaurants/deleteImage/{restaurantId}/{imageId}", method = RequestMethod.GET)
    public String deleteRestaurantHandler(@PathVariable("restaurantId") long restaurantId,
                                          @PathVariable("imageId") long imageId,
                                          Model model) {
            Restaurant restaurant = restaurantService.findById(restaurantId).get();

            Photo photo = photoService.findById(imageId).get();
            restaurant.getPhotos().remove(photo);
            restaurantService.save(restaurant);
            photoService.delete(photo);

        return "redirect:/restaurants/edit/" + restaurantId;
    }

    @RequestMapping(value = "/dishes/deleteImage/{dishId}/{imageId}", method = RequestMethod.GET)
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

    @RequestMapping(value = "/image/{url}")
    @ResponseBody
    public HttpEntity<byte[]> getImage(@PathVariable(value = "url") String url) {

        Photo photo = photoService.getPhotoByUrl(url);
        if(photo == null) {
            return new HttpEntity<>(null, new HttpHeaders());
        }
        byte[] array = photoService.getPhotoByUrl(url).getImage();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(array.length);

        return new HttpEntity<>(array, headers);
    }
}
