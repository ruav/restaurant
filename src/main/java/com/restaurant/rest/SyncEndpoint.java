package com.restaurant.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.dto.SseMessage;
import com.restaurant.entity.Client;
import com.restaurant.entity.Hostess;
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
import java.io.IOException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static com.restaurant.utils.DtoConverter.getClientDto;
import static com.restaurant.utils.DtoConverter.getHostessDto;

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
    HostessService hostessService;

    @Autowired
    TagService tagService;

    @Autowired
    ClientService clientService;

    @Autowired
    NotificationService notificationService;

    private static final long RESTAURANT = 1l; // only for test.

    private static ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/hostess")
    public List<Hostess> hostessList(@PathParam("from") int from,
                                    @PathParam("to") int to,
                                    @PathParam("limit") int limit,
                                    @PathParam("offset") int offset,
                                    @PathParam("restaurantId") int restaurantId,
                                    HttpServletRequest request) {
//        long restaurantId = getRestaurantId(request.getCookies());

//        if (!checkAuth(request.getCookies(), restaurantId)) {
//            return Collections.EMPTY_LIST;
//        }
        return hostessService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset);
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
                new SseMessage("client",
                mapper.writeValueAsString(getClientDto(clientService.findById(id).get()))));
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
                new SseMessage("client",
                mapper.writeValueAsString(getClientDto(clientService.findById(id).get()))));
        return id;
    }


    @PostMapping("/create/hostess")
    public long createHostess(@RequestParam String name,
                             HttpServletRequest request) throws JsonProcessingException {

//        long restaurantId = getRestaurantId(request.getCookies());
//
//        if (!checkAuth(request.getCookies(), restaurantId)) {
//            return -1;
//        }
        Hostess hostess = new Hostess();
        hostess.setName(name);
        long id = hostessService.save(hostess).getId();
        notificationService.addElement(RESTAURANT,
                new SseMessage("hostess",
                mapper.writeValueAsString(getHostessDto(hostessService.findById(id).get(), ""))));
        return id;
    }

    @PostMapping("/update/hostess")
    public long updateHostess(@RequestParam long id,
                             @RequestParam @NotEmpty String name,
                             HttpServletRequest request) throws JsonProcessingException {

//        long restaurantId = getRestaurantId(request.getCookies());
//
//        if (!checkAuth(request.getCookies(), restaurantId)) {
//            return -1;
//        }
        Hostess hostess = new Hostess();
        hostess.setId(id);
        hostess.setName(name);
        hostessService.save(hostess);
        notificationService.addElement(RESTAURANT,
                new SseMessage("hostess",
                mapper.writeValueAsString(getHostessDto(hostessService.findById(id).get(), ""))));
        return id;
    }

    @GetMapping("/sync")
    public SseEmitter streamSseMvc() throws IOException {
        SseEmitter emitter = new SseEmitter(1_000_000L);

        CustomSseEmitter customSseEmitter = new CustomSseEmitter(emitter, RESTAURANT, notificationService);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("UTC")));
        SseEmitter.SseEventBuilder event = SseEmitter.event()
                .data(calendar.getTimeInMillis())
                .id(String.valueOf(System.currentTimeMillis()))
                .reconnectTime(3 * 1_000)
                .name("timestamp");
        customSseEmitter.send(event);
        notificationService.addEmitter(customSseEmitter);

        notificationService.addEmitter(new CustomSseEmitter(emitter, RESTAURANT, notificationService));

        return emitter;
    }

}
