package com.restaurant.rest;

import com.restaurant.dto.AllergenDto;
import com.restaurant.dto.CategoryDto;
import com.restaurant.dto.ProteinDto;
import com.restaurant.dto.RestaurantModel;
import com.restaurant.entity.Category;
import com.restaurant.entity.Dish;
import com.restaurant.entity.Restaurant;
import com.restaurant.entity.SubCategory;
import com.restaurant.service.AllergenService;
import com.restaurant.service.CategoryService;
import com.restaurant.service.DishService;
import com.restaurant.service.ProteinService;
import com.restaurant.service.RestaurantService;
import com.restaurant.service.SubCategoryService;
import com.restaurant.utils.Converter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest")
@Api(value = "PublicEndpoint")
public class PublicEndpoint {

    @Autowired
    CategoryService categoryService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    DishService dishService;

    @Autowired
    SubCategoryService subCategoryService;

    @Autowired
    AllergenService allergenService;

    @Autowired
    ProteinService proteinService;

    @Autowired
    Converter converter;

    /**
     * Возвращает данные по выбранному ресторану
     * @param restaurantId id ресторана
     * @return результат в формате json.
     */
    @ApiOperation(value ="Возвращает данные по выбранному ресторану", response = RestaurantModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(value = "/restaurant/{restaurantId}")
    public RestaurantModel restaurant(@ApiParam(value = "id ресторана.", example = "1", required = true) @PathVariable("restaurantId") long restaurantId, HttpServletRequest request) {
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/image/";

        RestaurantModel restaurantModel = new RestaurantModel();

        Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);

        if (!restaurant.isPresent())
            return restaurantModel;

        restaurantModel.setLogo(restaurant.get().getLogo() == null ? null : (url + restaurant.get().getLogo().getUrl()));
        List<AllergenDto> allergens = allergenService.findAll().stream().map(Converter::getAllergenDto).collect(Collectors.toList());
        restaurantModel.setAllergens(allergens);
        List<ProteinDto> proteins = proteinService.findAll().stream().map(Converter::getProteinDto).collect(Collectors.toList());
        restaurantModel.setProteins(proteins);
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categoryService.findByRestaurant(restaurantId)) {

            List<SubCategory> subCategories = subCategoryService.findByCategoryId(category.getId());
            Map<Long, List<Dish>> collect = subCategories.stream().collect(Collectors.toMap(SubCategory::getId, subCategory -> dishService.findBySubCategoryId(subCategory.getId()), (a, b) -> b));
            CategoryDto categoryDto = Converter.getCategoryDto(category, subCategories,
                    collect, url);
            categoryDtos.add(categoryDto);
        }

        restaurantModel.setCategoryDtos(categoryDtos);

        return restaurantModel;
    }


}
