package com.restaurant.utils;

import com.restaurant.dto.*;
import com.restaurant.entity.*;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DtoConverter {

    private static String image = "/image/";
    private static String icon = "/icon/";

    private DtoConverter() {
    }

    public static CategoryDto getCategoryDto(Category category, List<SubCategory> subCategories, Map<Long, List<Dish>> map, String url) {
        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setPhoto(category.getLogo() == null ? null : url + image + category.getLogo().getUrl());
        subCategories.forEach(s -> categoryDto.getSubCategories().add(getSubCategoryDto(s, map.get(s.getId()), url)));
        return categoryDto;
    }

    public static SubCategoryDto getSubCategoryDto(SubCategory s, List<Dish> dishCollection, String url) {
        SubCategoryDto dto = new SubCategoryDto();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setPhoto(s.getLogo() == null ? null : url + image + s.getLogo().getUrl());
        List<DishDto> dishes = dishCollection.stream().map(dish -> getDishDto(dish, url)).collect(Collectors.toList());
        dto.setDishes(dishes);
        return dto;
    }


    public static DishDto getDishDto(Dish dish, String url) {
        DishDto dto = new DishDto();

        dto.setId(dish.getId());
        dto.setPrice(dish.getPrice());
        dto.setName(dish.getName());
        dto.setDescription(dish.getDescription());
        dto.setPrice(dish.getPrice());
        dto.setCalories(dish.getCalories());
        dto.setPhoto(dish.getLogo() == null ? null : url + image + dish.getLogo().getUrl());
        dto.setIngredients(dish.getIngredients().stream().map(i -> getIngredientDto(i, url)).collect(Collectors.toList()));
        dto.setSalt(dish.getSalt());
        dto.setCarbohydrates(dish.getCarbohydrates());
        dto.setCellulose(dish.getCellulose());
        dto.setFats(dish.getFats());
        dto.setFiber(dish.getFiber());
        dto.setSaturatedFats(dish.getSaturatedFats());
        dto.setSugar(dish.getSugar());
        dto.setVideo(dish.getVideo());
        dto.setWeight(dish.getWeight());
        List<Long> allergens = dish.getAllergens().stream().map(Allergen::getId).collect(Collectors.toList());
        List<Long> proteins = dish.getProteins().stream().map(Protein::getId).collect(Collectors.toList());

        dto.setProteins(proteins);
        dto.setAllergens(allergens);
        return dto;

    }

    public static IngredientDto getIngredientDto(Ingredient ingredient, String url) {
        IngredientDto dto = new IngredientDto();
        dto.setId(ingredient.getId());
        dto.setName(ingredient.getName());
        dto.setIcon(ingredient.getLogo() == null ? (url + icon + "0") : url + icon + ingredient.getLogo().getUrl());
        return dto;
    }

    public static ProteinDto getProteinDto(Protein protein) {
        ProteinDto dto = new ProteinDto();
        dto.setId(protein.getId());
        dto.setName(protein.getName());

        return dto;
    }

    public static AllergenDto getAllergenDto(Allergen allergen) {
        AllergenDto dto = new AllergenDto();
        dto.setId(allergen.getId());
        dto.setName(allergen.getName());
        return dto;
    }

    public static RestaurantDto getRestaurantDto(Restaurant restaurant) {
        RestaurantDto dto = new RestaurantDto();
        dto.setId(restaurant.getId());
        dto.setLatitude(restaurant.getLatitude());
        dto.setLongtitude(restaurant.getLongtitude());
        return dto;
    }

    public static PhotoDto getPhotoDto(Image photo, String url) {
        PhotoDto photoDto = new PhotoDto();
        photoDto.setId(photo.getId());
        photoDto.setUrl(url + "/" + photo.getClass().getSimpleName().toLowerCase() + "/" + photo.getUrl());
        return photoDto;
    }

    public static ClientDto getClientDto(Client client) {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(client.getId());
        clientDto.setName(client.getName());
        clientDto.setPhone(client.getPhone());
        clientDto.setVip(client.isVip());
        clientDto.setLastChange(client.getLastChange());
        List<Long> list = client.getTags().stream().map(Tag::getId).collect(Collectors.toList());
        clientDto.setTags(list);
        return clientDto;
    }

    public static HostesDto getHostesDto(Hostes hostes, String url) {
        HostesDto dto = new HostesDto();
        dto.setId(hostes.getId());
        dto.setLastChange(hostes.getLastChange());
        dto.setName(hostes.getName());
        dto.setWork(hostes.isWork());
        dto.setPhoto(getPhotoDto(hostes.getPhoto(), url));
        return dto;
    }

    public static TagDto getTagDto(Tag tag) {
        TagDto dto = new TagDto();
        dto.setId(tag.getId());
        dto.setLastChange(tag.getLastChange());
        dto.setName(tag.getName());
        return dto;
    }

    public static HallDto getHallDto(Hall hall) {
        HallDto dto = new HallDto();
        dto.setId(hall.getId());
        dto.setActive(hall.isActive());
        dto.setName(hall.getName());
        dto.setOnline(hall.isOnline());
        dto.setLastChange(hall.getLastChange());
        return dto;
    }

    public static DeskDto getDeskDto(Desk desk) {
        DeskDto dto = new DeskDto();
        dto.setId(desk.getId());
        dto.setCapacity(desk.getCapacity());
        dto.setHallId(desk.getHall());
        dto.setNumber(desk.getNumber());
        dto.setLastChange(desk.getLastChange());
        return dto;
    }

    public static CardDto getCardDto(Card card) {
        CardDto dto = new CardDto();
        dto.setId(card.getId());
        dto.setHallId(card.getHall());
        dto.setMap(card.getMap());
        dto.setRelevantFrom(getDate(card.getRelevantFrom()));
        dto.setLastChange(card.getLastChange());
        return dto;
    }

    public static ReservationDto getReservationDto(Reservation reservation) {
        ReservationDto dto = new ReservationDto();
        dto.setDate(getDate(reservation.getDate()));
        dto.setId(reservation.getId());
        dto.setClient(reservation.getClientId());
        dto.setGuests(reservation.getGuests());
        dto.setLastchange(reservation.getLastChange());
        dto.setTags(reservation.getTags().stream().map(DtoConverter::getTagDto).collect(Collectors.toList()));
        dto.setTables(reservation.getDesks().stream().map(DtoConverter::getDeskDto).collect(Collectors.toList()));
        dto.setTimeFrom(getTime(reservation.getTimeFrom()));
        dto.setTimeTo(getTime(reservation.getTimeTo()));
        return dto;
    }

    private static String getDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
        return simpleDateFormat.format(date);
    }

    private static String getTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH-mm-ss");
        return simpleDateFormat.format(date);
    }
}
