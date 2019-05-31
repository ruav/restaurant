package com.restaurant.rest;

import com.restaurant.dto.AllergenDto;
import com.restaurant.dto.CategoryDto;
import com.restaurant.dto.ProteinDto;
import com.restaurant.dto.RestaurantMenuModel;
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
import org.springframework.http.MediaType;
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
    @ApiOperation(value ="Возвращает меню выбранного ресторана", response = RestaurantMenuModel.class, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(value = "/menu/{restaurantId}")
    public RestaurantMenuModel menu(@ApiParam(value = "id ресторана.", example = "1", required = true) @PathVariable("restaurantId") long restaurantId, HttpServletRequest request) {
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() ;

        RestaurantMenuModel restaurantMenuModel = new RestaurantMenuModel();

        Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);

        if (!restaurant.isPresent())
            return restaurantMenuModel;

        restaurantMenuModel.setLogo(restaurant.get().getLogo() == null ? null : (url + "/image/" + restaurant.get().getLogo().getUrl()));
        List<AllergenDto> allergens = allergenService.findAll().stream().map(Converter::getAllergenDto).collect(Collectors.toList());
        restaurantMenuModel.setAllergens(allergens);
        List<ProteinDto> proteins = proteinService.findAll().stream().map(Converter::getProteinDto).collect(Collectors.toList());
        restaurantMenuModel.setProteins(proteins);

        List<String> actions = restaurant.get().getActions().stream().map(action -> url + "/image/" + action.getUrl()).collect(Collectors.toList());

        restaurantMenuModel.setActions(actions);
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categoryService.findByRestaurant(restaurantId)) {

            List<SubCategory> subCategories = subCategoryService.findByCategoryId(category.getId());
            Map<Long, List<Dish>> collect = subCategories.stream().collect(Collectors.toMap(SubCategory::getId, subCategory -> dishService.findBySubCategoryId(subCategory.getId()), (a, b) -> b));
            CategoryDto categoryDto = Converter.getCategoryDto(category, subCategories,
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
                "\t\t\t\"youtube\": \"SPauxWv1Rnk\",\n" +
                "\t\t\t\"description\": \"Приложение \\\"Забей\\\" помогает:\\n- узнать, где сегодня будет туса;\\n- проверить наличие свободных мест;\\n- посмотреть 360-панораму зала;\\n- забронировать нужный столик;\\n- оплатить депозит;\\n- посмотреть меню;\\n- позвать официанта к столику.\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"title\": \"Добавить заведение\",\n" +
                "\t\t\t\"youtube\": null,\n" +
                "\t\t\t\"description\": \"Чтобы добавить свое заведение в приложение, свяжитесь с администратором нашего сообщества Вконтакте.\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"title\": \"Франшиза\",\n" +
                "\t\t\t\"youtube\": \"SPauxWv1Rnk\",\n" +
                "\t\t\t\"description\": \"С нами можно заработать. Подробнее о франшизе можете прочитать в нашей группе Вконтакте\"\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"authors\": {\n" +
                "\t\t\"title\": \"Волшебники\",\n" +
                "\t\t\"photos\": [\n" +
                "\t\t\t\"https://zabei.app/img/person.jpg\",\n" +
                "\t\t\t\"https://zabei.app/img/person.jpg\",\n" +
                "\t\t\t\"https://zabei.app/img/person.jpg\"\n" +
                "\t\t]\n" +
                "\t}\n" +
                "}";
    }


}
