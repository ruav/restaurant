package com.restaurant.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.entity.Client;
import com.restaurant.entity.Hostes;
import com.restaurant.entity.Tag;
import com.restaurant.service.*;
import com.restaurant.utils.DtoConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.thymeleaf.util.ArrayUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.restaurant.rest.AuthorityEndpoint.ALGORITHM;

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

    private static final int LIMIT = 30;
    private final static String AUTHORIZATION = "Authorization";
    private final static String ISSUER = "auth0";

    private Queue<String> queue = new LinkedList<>();

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
        long restaurantId = getRestaurantId(request.getCookies());

//        if (!checkAuth(request.getCookies(), restaurantId)) {
//            return -1;
//        }
        Client client = new Client();

        client.setName(name);
        client.setPhone(phone);
        client.setVip(vip);
        long id = clientService.save(client).getId();
        queue.add(mapper.writeValueAsString(clientService.findById(id).get()));
        return id;
    }

    @PostMapping("/update/client")
    public long updateClient(@RequestParam long id,
                             @RequestParam String name,
                             @RequestParam @NotNull String phone,
                             @RequestParam boolean vip,
                             HttpServletRequest request
    ) throws JsonProcessingException {
        long restaurantId = getRestaurantId(request.getCookies());

//        if (!checkAuth(request.getCookies(), restaurantId)) {
//            return -1;
//        }
        Client client = new Client();
        client.setId(id);
        client.setName(name);
        client.setPhone(phone);
        client.setVip(vip);
        clientService.save(client);
        queue.add(mapper.writeValueAsString(clientService.findById(id).get()));
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
        queue.add(mapper.writeValueAsString(hostesService.findById(id).get()));
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
        queue.add(mapper.writeValueAsString(hostesService.findById(id).get()));
        return id;
    }

    @GetMapping("/sync")
    public SseEmitter streamSseMvc() {
        SseEmitter emitter = new SseEmitter();
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        sseMvcExecutor.execute(() -> {
            try {
                int i = 0;
                while (true) {
                    if (!queue.isEmpty()) {
                        String data = queue.poll();
                        SseEmitter.SseEventBuilder event = SseEmitter.event()
                                .data(data)
                                .id(String.valueOf(i))
                                .reconnectTime(3 * 1_000)
                                .name("event");
                        emitter.send(event);
                        i++;
                    }
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }

    private long getRestaurantId(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (AUTHORIZATION.equals(cookie.getName())) {
                JWTVerifier verifier = JWT.require(ALGORITHM)
                        .withIssuer(ISSUER)
                        .build(); //Reusable verifier instance
                DecodedJWT jwt = verifier.verify(cookie.getValue());
                if (jwt.getClaim("id").asLong() != null) {
                    return jwt.getClaim("id").asLong();
                }
            }
        }
        return 0;
    }

    private boolean checkAuth(Cookie[] cookies, long id) {
        if (ArrayUtils.isEmpty(cookies)) return false;
        for (Cookie cookie : cookies) {
            if (checkAccess(cookie, id)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkAccess(Cookie cookie, long id) {
        if (AUTHORIZATION.equals(cookie.getName())) {
            JWTVerifier verifier = JWT.require(ALGORITHM)
                    .withIssuer(ISSUER)
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(cookie.getValue());
            if (id == jwt.getClaim("id").asLong() && jwt.getExpiresAt().after(Calendar.getInstance().getTime())) {
                return true;
            }
        }
        return false;
    }

}
