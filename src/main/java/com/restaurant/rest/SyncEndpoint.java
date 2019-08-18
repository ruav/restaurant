package com.restaurant.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.entity.Client;
import com.restaurant.entity.Hostes;
import com.restaurant.entity.Tag;
import com.restaurant.service.*;
import com.restaurant.utils.CustomSseEmitter;
import com.restaurant.utils.DtoConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.util.List;

import static com.restaurant.utils.DtoConverter.getClientDto;
import static com.restaurant.utils.DtoConverter.getHostesDto;

@RestController
@RequestMapping("/rest")
@Api(value = "SyncEndpoint")
public class SyncEndpoint {

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

    @Autowired
    HostesService hostesService;

    @Autowired
    TagService tagService;

    @Autowired
    ClientService clientService;

    @Autowired
    NotificationService notificationService;

    private static final long RESTAURANT = 1l; // only for test.

    private static ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/hostes")
    public List<Hostes> hostesList(@PathParam("from") int from,
                                   @PathParam("to") int to,
                                   @PathParam("limit") int limit,
                                   @PathParam("offset") int offset,
                                   @PathParam("restaurantId") int restaurantId,
                                   HttpServletRequest request) {
//        long restaurantId = getRestaurantId(request.getCookies());

//        if (!checkAuth(request.getCookies(), restaurantId)) {
//            return Collections.EMPTY_LIST;
//        }
        return hostesService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset);
    }

    @GetMapping("/tags")
    public List<Tag> tagsList(@PathParam("from") int from,
                              @PathParam("to") int to,
                              @PathParam("limit") int limit,
                              @PathParam("offset")int offset,
                              @PathParam("restaurantId")int restaurantId,
                              HttpServletRequest request) {
//        long restaurantId = getRestaurantId(request.getCookies());
//
//        if (!checkAuth(request.getCookies(), restaurantId)) {
//            return Collections.EMPTY_LIST;
//        }
        return tagService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset);
    }

    @GetMapping("/clients")
    public List<Client> clientList(@PathParam("from") int from,
                                   @PathParam("to") int to,
                                   @PathParam("limit") int limit,
                                   @PathParam("offset")int offset,
                                   @PathParam("restaurantId")int restaurantId,
                                   HttpServletRequest request) {
//        long restaurantId = getRestaurantId(request.getCookies());
//
//        if (!checkAuth(request.getCookies(), restaurantId)) {
//            return Collections.EMPTY_LIST;
//        }
        return clientService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset);
    }

    @PostMapping("/create/client")
    public long createClient(@RequestParam String name,
                             @RequestParam @NotNull String phone,
                             @RequestParam boolean vip,
                             HttpServletRequest request
                             ) throws JsonProcessingException {

//        if (!checkAuth(request.getCookies(), restaurantId)) {
//            return -1;
//        }
        Client client = new Client();

        client.setName(name);
        client.setPhone(phone);
        client.setVip(vip);
        long id = clientService.save(client).getId();
        notificationService.addElement(RESTAURANT,
                mapper.writeValueAsString(getClientDto(clientService.findById(id).get())));
        return id;
    }

    @PostMapping("/update/client")
    public long updateClient(@RequestParam long id,
                             @RequestParam String name,
                             @RequestParam @NotNull String phone,
                             @RequestParam boolean vip,
                             HttpServletRequest request
    ) throws JsonProcessingException {

//        if (!checkAuth(request.getCookies(), restaurantId)) {
//            return -1;
//        }
        Client client = new Client();
        client.setId(id);
        client.setName(name);
        client.setPhone(phone);
        client.setVip(vip);
        clientService.save(client);
        notificationService.addElement(RESTAURANT,
                mapper.writeValueAsString(getClientDto(clientService.findById(id).get())));
        return id;
    }


    @PostMapping("/create/hostes")
    public long createHostes(@RequestParam String name,
                             HttpServletRequest request) throws JsonProcessingException {

//        long restaurantId = getRestaurantId(request.getCookies());
//
//        if (!checkAuth(request.getCookies(), restaurantId)) {
//            return -1;
//        }
        Hostes hostes = new Hostes();
        hostes.setName(name);
        long id = hostesService.save(hostes).getId();
        notificationService.addElement(RESTAURANT,
                mapper.writeValueAsString(getHostesDto(hostesService.findById(id).get(), "")));
        return id;
    }

    @PostMapping("/update/hostes")
    public long updateHostes(@RequestParam long id,
                             @RequestParam @NotEmpty String name,
                             HttpServletRequest request) throws JsonProcessingException {

//        long restaurantId = getRestaurantId(request.getCookies());
//
//        if (!checkAuth(request.getCookies(), restaurantId)) {
//            return -1;
//        }
        Hostes hostes = new Hostes();
        hostes.setId(id);
        hostes.setName(name);
        hostesService.save(hostes);
        notificationService.addElement(RESTAURANT,
                mapper.writeValueAsString(getHostesDto(hostesService.findById(id).get(), "")));
        return id;
    }

    @GetMapping("/sync")
    public SseEmitter streamSseMvc() {
        SseEmitter emitter = new SseEmitter(1_000_000L);

        notificationService.addEmitter(new CustomSseEmitter(emitter, RESTAURANT, notificationService));

        return emitter;
    }

}
