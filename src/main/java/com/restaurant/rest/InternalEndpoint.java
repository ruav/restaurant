package com.restaurant.rest;

import com.restaurant.dto.PhotoDto;
import com.restaurant.entity.Icon;
import com.restaurant.entity.Ingredient;
import com.restaurant.service.IconService;
import com.restaurant.service.IngredientService;
import com.restaurant.utils.DtoConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/internal")
@Api(value = "InternalEndpoint")
public class InternalEndpoint {

    @Autowired
    IngredientService ingredientService;

    @Autowired
    IconService iconService;

    @PostMapping(value = "/createingredient", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Long createIngredient(@RequestBody String name, @PathParam("iconId") long iconId) {

        Ingredient ingredient = ingredientService.findByName(name);
        if (ingredient == null) {
            ingredient = new Ingredient();
            ingredient.setName(name);
            Optional<Icon> icon = iconService.findById(iconId);
            if (icon.isPresent()) {
                ingredient.setLogo(icon.get());
            }
            return ingredientService.save(ingredient).getId();
        }
        return null;
    }

    @GetMapping(value = "/icons")
    public List<PhotoDto> getIcons(HttpServletRequest request) {
        String url;
        if (request.getServerName().equalsIgnoreCase("localhost") || request.getServerName().equalsIgnoreCase("127.0.0.1")) {
            url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        } else {
            url = "https://" + request.getServerName();
        }
        return iconService.findAll().stream().map(i -> DtoConverter.getPhotoDto(i, url)).collect(Collectors.toList());
    }

}
