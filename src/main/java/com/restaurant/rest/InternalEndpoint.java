package com.restaurant.rest;

import com.restaurant.entity.Ingredient;
import com.restaurant.service.IngredientService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@Api(value = "PublicEndpoint")
public class InternalEndpoint {

    @Autowired
    IngredientService ingredientService;

    @PostMapping(value = "/createingredient", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Long createIngredient(@RequestBody String name) {

        Ingredient ingredient = ingredientService.findByName(name);
        if (ingredient == null) {
            ingredient = new Ingredient();
            ingredient.setName(name);
            return ingredientService.save(ingredient).getId();
        }
        return null;
    }

}
