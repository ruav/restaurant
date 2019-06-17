package com.restaurant.service;

import com.restaurant.Exception.ForbiddenException;
import com.restaurant.Exception.NotFoundException;
import com.restaurant.vo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CheckAccess {

    @Autowired
    private UserService userService;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SubCategoryService subCategoryService;
    @Autowired
    private HallService hallService;

    public void checkAccessRestaurant(UserDetails userDetails, long id) {
        if (!restaurantService.findById(id).isPresent()) {
            throw new NotFoundException();
        }
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Role.Root.name())) && !userService.findByEmail(userDetails.getUsername()).getRestaurants().contains(restaurantService.findById(id).get())) {
            throw new ForbiddenException();
        }
    }

    public void checkAccessCategory(UserDetails userDetails, long id) {
        if (!categoryService.findById(id).isPresent()) {
            throw new NotFoundException();
        }
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Role.Root.name()))) {
            return;
        } else if (userService.findByEmail(userDetails.getUsername()).getRestaurants().stream().noneMatch(r -> r.getId() == categoryService.findById(id).get().getRestaurantId())) {
            throw new ForbiddenException();
        }
    }

    public void checkAccessSubCategory(UserDetails userDetails, long id) {
        if (!subCategoryService.findById(id).isPresent() || !categoryService.findById(subCategoryService.findById(id).get().getCategoryId()).isPresent()) {
            throw new NotFoundException();
        }
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Role.Root.name()))) {
            return;
        } else if (userService.findByEmail(userDetails.getUsername()).getRestaurants().stream().noneMatch(r -> r.getId() == categoryService.findById(subCategoryService.findById(id).get().getCategoryId()).get().getRestaurantId())) {
            throw new ForbiddenException();
        }
    }

    public void checkAccessHall(UserDetails userDetails, long id) {
        if (!hallService.findById(id).isPresent() || !restaurantService.findById(hallService.findById(id).get().getRestaurantId()).isPresent()) {
            throw new NotFoundException();
        }
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Role.Root.name())) && !userService.findByEmail(userDetails.getUsername())
                .getRestaurants().contains(restaurantService.findById(hallService.findById(id).get().getRestaurantId()).get())) {
            throw new ForbiddenException();
        }
    }

}
