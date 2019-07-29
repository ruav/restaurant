package com.restaurant.rest;

import com.restaurant.dto.AllergenDto;
import com.restaurant.dto.CategoryDto;
import com.restaurant.dto.IngredientDto;
import com.restaurant.dto.ProteinDto;
import com.restaurant.dto.RestaurantDto;
import com.restaurant.dto.RestaurantMenuModel;
import com.restaurant.entity.Category;
import com.restaurant.entity.Dish;
import com.restaurant.entity.Ingredient;
import com.restaurant.entity.Restaurant;
import com.restaurant.entity.SubCategory;
import com.restaurant.service.AllergenService;
import com.restaurant.service.CategoryService;
import com.restaurant.service.DishService;
import com.restaurant.service.IngredientService;
import com.restaurant.service.ProteinService;
import com.restaurant.service.RestaurantService;
import com.restaurant.service.SubCategoryService;
import com.restaurant.utils.DtoConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.HashMap;
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
    DtoConverter dtoConverter;

    @Autowired
    IngredientService ingredientService;

    private static final int LIMIT = 30;

    /**
     * Возвращает данные по выбранному ресторану
     * @param restaurantId id ресторана
     * @return результат в формате json.
     */
    @ApiOperation(value ="Возвращает меню выбранного ресторана", response = RestaurantMenuModel.class, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(value = "/menu/{restaurantId}")
    public RestaurantMenuModel menu(@ApiParam(value = "id ресторана.", example = "1", required = true) @PathVariable("restaurantId") long restaurantId, HttpServletRequest request) {
        String url;
        if (request.getServerName().equalsIgnoreCase("localhost") || request.getServerName().equalsIgnoreCase("127.0.0.1")) {
            url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        } else {
            url = "https://" + request.getServerName();
        }

        RestaurantMenuModel restaurantMenuModel = new RestaurantMenuModel();

        Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);

        if (!restaurant.isPresent() || !restaurant.get().isActive())
            return restaurantMenuModel;

        restaurantMenuModel.setLogo(restaurant.get().getLogo() == null ? null : (url + "/image/" + restaurant.get().getLogo().getUrl()));
        List<AllergenDto> allergens = allergenService.findAll().stream().map(DtoConverter::getAllergenDto).collect(Collectors.toList());
        restaurantMenuModel.setAllergens(allergens);
        List<ProteinDto> proteins = proteinService.findAll().stream().map(DtoConverter::getProteinDto).collect(Collectors.toList());
        restaurantMenuModel.setProteins(proteins);

        List<String> actions = restaurant.get().getActions().stream().map(action -> url + "/image/" + action.getUrl()).collect(Collectors.toList());

        restaurantMenuModel.setActions(actions);
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categoryService.findActiveByRestaurant(restaurantId)) {

            List<SubCategory> subCategories = subCategoryService.findActiveByCategoryId(category.getId());
            Map<Long, List<Dish>> collect = subCategories.stream().collect(Collectors.toMap(SubCategory::getId, subCategory -> dishService.findActiveBySubCategoryId(subCategory.getId()), (a, b) -> b));
            CategoryDto categoryDto = DtoConverter.getCategoryDto(category, subCategories,
                    collect, url);
            categoryDtos.add(categoryDto);
        }

        restaurantMenuModel.setCategories(categoryDtos);

        return restaurantMenuModel;
    }

    @ApiOperation(value ="about", response = String.class, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(value = "/about", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String about() {
        return "{\n" +
                "\t\"vk\": \"https://vk.com/zabeiapp\",\n" +
                "\t\"instagram\": \"https://www.instagram.com/zabeiapp/\",\n" +
                "\t\"about\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"title\": \"О приложении\",\n" +
                "\t\t\t\"youtube\": null,\n" +
                "\t\t\t\"description\": \"Через приложение \\\"Забей\\\" можно посмотреть электронное меню ресторанов, кафе, кальянных и ночных клубов.\\n1. Установите приложение\\n2. Посетите заведение, подключенное к нашей системе\\n3. Считайте QR-код, размещенный на столике\\n4. Выберите, что будете есть\\n5. Наслаждайтесь\\n\\nСкоро в нашем приложении появятся и другие функции. Так что можете следить за новостями в нашей группе Вконтакте и ждать обновлений с новым функционалом.\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"title\": \"Добавить заведение\",\n" +
                "\t\t\t\"youtube\": null,\n" +
                "\t\t\t\"description\": \"Чтобы добавить свое заведение в приложение, свяжитесь с администратором нашего сообщества Вконтакте.\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"title\": \"Франшиза\",\n" +
                "\t\t\t\"youtube\": null,\n" +
                "\t\t\t\"description\": \"С нами можно заработать. Подробнее о франшизе можете прочитать в нашей группе Вконтакте\"\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"authors\": {\n" +
                "\t\t\"title\": \"Волшебники\",\n" +
                "\t\t\"photos\": [\n" +
                "\t\t\t\"https://zabei.app/img/person.jpg\"\n" +
                "\t\t]\n" +
                "\t}\n" +
                "}";
    }

    @ApiOperation(value ="restaurants", response = String.class, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(value = "/restaurants", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<RestaurantDto> findRestaurant() {
        return  restaurantService.findByCoordinate()
                    .stream().map(DtoConverter::getRestaurantDto).collect(Collectors.toList());
    }

    @ApiOperation(value ="/ingredients/search", response = String.class, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(value = "/ingredients/search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, Object> findIngredients(@PathParam("search") String search, @PathParam("page") Integer page) {
        Map<String, Object> objects = new HashMap<>();
        if (page == null ) {
            page = 0;
        } else if (page > 0){
            page--;
        }

        if (search == null) {
            search = "";
        }
        Pageable pageable = new PageRequest(page, LIMIT);
        List<Ingredient> ingredients = new ArrayList<>();
        if (search == null || search.isEmpty()) {
            ingredients = ingredientService.findAll(pageable);
        } else {
            ingredients = ingredientService.findByNameIsLike(search, pageable);
        }
        List<IngredientDto> result = ingredients.stream().map((Ingredient ingredient) -> DtoConverter.getIngredientDto(ingredient,"")).collect(Collectors.toList());
        objects.put("results", result);
        boolean more = (result.size() == LIMIT);
        objects.put("more", more);
        return objects;
    }

}
