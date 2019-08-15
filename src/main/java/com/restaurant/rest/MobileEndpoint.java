package com.restaurant.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.entity.Card;
import com.restaurant.entity.Client;
import com.restaurant.entity.Desk;
import com.restaurant.entity.Hall;
import com.restaurant.entity.Hostes;
import com.restaurant.entity.Tag;
import com.restaurant.service.*;
import com.restaurant.utils.DtoConverter;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.restaurant.rest.AuthorityEndpoint.ALGORITHM;
import static com.restaurant.utils.DtoConverter.getCardDto;
import static com.restaurant.utils.DtoConverter.getClientDto;
import static com.restaurant.utils.DtoConverter.getDeskDto;
import static com.restaurant.utils.DtoConverter.getHallDto;
import static com.restaurant.utils.DtoConverter.getHostesDto;
import static com.restaurant.utils.DtoConverter.getTagDto;

@RestController
@RequestMapping("/mobile")
@Api(value = "MobileEndpoint")
public class MobileEndpoint {

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
    UserService userService;
    @Autowired
    HallService hallService;
    @Autowired
    DeskService deskService;
    @Autowired
    CardService cardService;

    private static final int LIMIT = 30;
    private final static String AUTHORIZATION = "Authorization";
    private final static String ISSUER = "auth";

    private static Map<Long, Queue<String>> map = new ConcurrentHashMap<>();

    private static ObjectMapper mapper = new ObjectMapper();
    private static Logger logger = LoggerFactory.getLogger(MobileEndpoint.class);

    @GetMapping("/hostes")
    public List<Hostes> hostesList(@PathParam("from") int from,
                                   @PathParam("to") int to,
                                   @PathParam("limit") int limit,
                                   @PathParam("offset") int offset,
                                   @PathParam("restaurantId") int restaurantId,
                                   HttpServletRequest request) {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return hostesService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset);
        }
        return Collections.EMPTY_LIST;

    }

    @GetMapping("/tags")
    public List<Tag> tagsList(@PathParam("from") int from,
                              @PathParam("to") int to,
                              @PathParam("limit") int limit,
                              @PathParam("offset")int offset,
                              @PathParam("restaurantId")int restaurantId,
                              HttpServletRequest request) {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return tagService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset);
        }
        return Collections.EMPTY_LIST;
    }

    @GetMapping("/clients")
    public List<Client> clientList(@PathParam("from") int from,
                                   @PathParam("to") int to,
                                   @PathParam("limit") int limit,
                                   @PathParam("offset")int offset,
                                   @PathParam("restaurantId")int restaurantId,
                                   HttpServletRequest request) {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return clientService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset);
        }
        return Collections.EMPTY_LIST;
    }

    @GetMapping("/halls")
    public List<Hall> hallList(@PathParam("from") int from,
                                   @PathParam("to") int to,
                                   @PathParam("limit") int limit,
                                   @PathParam("offset")int offset,
                                   @PathParam("restaurantId")int restaurantId,
                                   HttpServletRequest request) {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return hallService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset);
        }
        return Collections.EMPTY_LIST;
    }

    @GetMapping("/desks")
    public List<Desk> deskList(@PathParam("from") int from,
                               @PathParam("to") int to,
                               @PathParam("limit") int limit,
                               @PathParam("offset")int offset,
                               @PathParam("restaurantId")int restaurantId,
                               HttpServletRequest request) {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return deskService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset);
        }
        return Collections.EMPTY_LIST;
    }

    @GetMapping("/cards")
    public List<Card> cardList(@PathParam("from") int from,
                               @PathParam("to") int to,
                               @PathParam("limit") int limit,
                               @PathParam("offset")int offset,
                               @PathParam("restaurantId")int restaurantId,
                               HttpServletRequest request) {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return cardService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset);
        }
        return Collections.EMPTY_LIST;
    }

    @PostMapping("/create/client")
    public long createClient(@RequestParam String name,
                             @RequestParam @NotNull String phone,
                             @RequestParam boolean vip,
                             HttpServletRequest request
                             ) throws JsonProcessingException {

        if (!checkTocken(request.getHeader(AUTHORIZATION))) {

            Client client = new Client();

            client.setName(name);
            client.setPhone(phone);
            client.setVip(vip);
            client.setLastChange(getTimeStamp());
            client.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
            long id = clientService.save(client).getId();
            addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                    mapper.writeValueAsString(getClientDto(clientService.findById(id).get())));
            return id;
        }
        return -1;
    }

    @PostMapping("/update/client")
    public long updateClient(@RequestParam long id,
                             @RequestParam String name,
                             @RequestParam @NotNull String phone,
                             @RequestParam boolean vip,
                             HttpServletRequest request
    ) throws JsonProcessingException {

        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        Client client = clientService.findById(id).get();
        client.setId(id);
        client.setName(name);
        client.setPhone(phone);
        client.setVip(vip);
        client.setLastChange(getTimeStamp());
        client.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        clientService.save(client);
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getClientDto(clientService.findById(id).get())));
        return id;
    }


    @PostMapping("/create/hostes")
    public long createHostes(@RequestParam String name,
                             HttpServletRequest request) throws JsonProcessingException {

        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        String url;
        if (request.getServerName().equalsIgnoreCase("localhost") || request.getServerName().equalsIgnoreCase("127.0.0.1")) {
            url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        } else {
            url = "https://" + request.getServerName();
        }

        Hostes hostes = new Hostes();
        hostes.setName(name);
        hostes.setLastChange(getTimeStamp());
        hostes.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        long id = hostesService.save(hostes).getId();
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getHostesDto(hostesService.findById(id).get(), url)));
        return id;
    }

    @PostMapping("/update/hostes")
    public long updateHostes(@RequestParam long id,
                             @RequestParam @NotEmpty String name,
                             HttpServletRequest request) throws JsonProcessingException {

        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        String url;
        if (request.getServerName().equalsIgnoreCase("localhost") || request.getServerName().equalsIgnoreCase("127.0.0.1")) {
            url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        } else {
            url = "https://" + request.getServerName();
        }
        Hostes hostes = hostesService.findById(id).get();
        hostes.setName(name);
        hostes.setLastChange(getTimeStamp());
        hostes.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        hostesService.save(hostes);
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getHostesDto(hostesService.findById(id).get(), url)));
        return id;
    }

    @PostMapping("/create/clientTag")
    public long createClientTag(@RequestParam String name,
                             @RequestParam long clientId,
                             HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        Tag tag = new Tag();
        tag.setClientId(clientId);
        tag.setName(name);
        tag.setLastChange(getTimeStamp());
        tag.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        long id = tagService.save(tag).getId();
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getTagDto(tagService.findById(id).get())));
        return id;
    }

    @PostMapping("/update/clientTag")
    public long updateClientTag(@RequestParam long id,
                             @RequestParam String name,
                             @RequestParam long clientId,
                             HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        Tag tag = new Tag();
        tag.setClientId(clientId);
        tag.setName(name);
        tag.setLastChange(getTimeStamp());
        tag.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        tagService.save(tag);
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getTagDto(tagService.findById(id).get())));
        return id;
    }

    @PostMapping("/create/hall")
    public long createHall(@RequestParam String name,
                             @RequestParam boolean active,
                             @RequestParam boolean online,
                             HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        Hall hall = new Hall();
        hall.setName(name);
        hall.setActive(active);
        hall.setOnline(online);
        hall.setLastChange(getTimeStamp());
        hall.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        long id = hallService.save(hall).getId();
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getHallDto(hallService.findById(id).get())));
        return id;
    }

    @PostMapping("/update/hall")
    public long updateHall(@RequestParam long id,
                             @RequestParam String name,
                             @RequestParam boolean active,
                             @RequestParam boolean online,
                             HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        Hall hall = hallService.findById(id).get();
        hall.setName(name);
        hall.setActive(active);
        hall.setOnline(online);
        hall.setLastChange(getTimeStamp());
        hall.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        hallService.save(hall);
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getHallDto(hallService.findById(id).get())));
        return id;
    }

    @PostMapping("/create/desk")
    public long createDesk(@RequestParam long hall,
                           @RequestParam int number,
                           @RequestParam int capacity,
                           HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        Desk desk = new Desk();
        desk.setHall(hall);
        desk.setNumber(number);
        desk.setCapacity(capacity);
        desk.setLastChange(getTimeStamp());
        desk.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        long id = deskService.save(desk).getId();
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getDeskDto(deskService.findById(id).get())));
        return id;
    }

    @PostMapping("/update/desk")
    public long updateDesk(@RequestParam long id,
                           @RequestParam long hall,
                           @RequestParam int number,
                           @RequestParam int capacity,
                           HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        Desk desk = deskService.findById(id).get();
        desk.setHall(hall);
        desk.setNumber(number);
        desk.setCapacity(capacity);
        desk.setLastChange(getTimeStamp());
        desk.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        deskService.save(desk);
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getDeskDto(deskService.findById(id).get())));
        return id;
    }

    @PostMapping("/create/Card")
    public long createCard(@RequestParam long hall,
                           @RequestParam String map,
                           @RequestParam Date relevantFrom,
                           HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        Card card = new Card();
        card.setHall(hall);
        card.setMap(map);
        card.setRelevantFrom(relevantFrom);
        card.setLastChange(getTimeStamp());
        card.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        long id = cardService.save(card).getId();
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getCardDto(cardService.findById(id).get())));
        return id;
    }

    @PostMapping("/update/card")
    public long updateCard(@RequestParam long id,
                           @RequestParam long hall,
                           @RequestParam String map,
                           @RequestParam Date relevantFrom,
                           HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        Card card = cardService.findById(id).get();
        card.setHall(hall);
        card.setMap(map);
        card.setRelevantFrom(relevantFrom);
        card.setLastChange(getTimeStamp());
        card.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        cardService.save(card).getId();
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getCardDto(cardService.findById(id).get())));
        return id;
    }

    @PostMapping("/create/reservationTag")
    public long createReservationTag(@RequestParam String name,
                                @RequestParam long clientId,
                                @RequestParam long reservationId,
                                HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        Tag tag = new Tag();
        tag.setName(name);
        tag.setReservationId(reservationId);
        tag.setLastChange(getTimeStamp());
        tag.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        long id = tagService.save(tag).getId();
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getTagDto(tagService.findById(id).get())));
        return id;
    }

    @PostMapping("/update/reservationTag")
    public long updateReservationTag(@RequestParam long id,
                                @RequestParam String name,
                                @RequestParam long reservationId,
                                HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        Tag tag = new Tag();
        tag.setName(name);
        tag.setReservationId(reservationId);
        tag.setLastChange(getTimeStamp());
        tag.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        tagService.save(tag);
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getTagDto(tagService.findById(id).get())));
        return id;
    }

    @GetMapping("/sync")
    public SseEmitter streamSseMvc(HttpServletRequest request,
                                   HttpServletResponse response) {
        if (!checkTocken(request.getHeader(AUTHORIZATION), true)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        SseEmitter emitter = new SseEmitter();
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
        sseMvcExecutor.execute(() -> {
            try {
                int i = 0;
                while (true) {
                    String data = getElement(restaurantId);
                    if (!data.isEmpty()) {
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

    private boolean checkTocken(String auth, boolean checkAccess) {
        JWTVerifier verifier = JWT.require(ALGORITHM)
                .withIssuer(ISSUER)
                .build();
        DecodedJWT jwt = verifier.verify(auth);

        if (jwt.getClaim("id").isNull()) return false;
        if (jwt.getClaim("restaurant").isNull()) return false;

        if (!checkAccess && jwt.getClaim("restaurant").asLong() != userService.findById(jwt.getClaim("id").asLong()).get().getRestaurants().stream().findFirst().get().getId()) {
            return true;
        }

        if (checkAccess &&
                jwt.getClaim("restaurant").asLong() == userService.findById(jwt.getClaim("id").asLong()).get().getRestaurants().stream().findFirst().get().getId()) {
            return true;
        }

        if (jwt.getExpiresAt().after(Calendar.getInstance().getTime())) {
            return true;
        }

        return false;
    }

    private boolean checkTocken(String auth) {
        return checkTocken(auth, false);
    }

    private long getRestaurantId(String auth) {
        JWTVerifier verifier = JWT.require(ALGORITHM)
                .withIssuer(ISSUER)
                .build();
        DecodedJWT jwt = verifier.verify(auth);
        return jwt.getClaim("restaurant").asLong();
    }

    private long getTimeStamp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return calendar.getTimeInMillis();
    }

    private String getElement(long restaurantId) {
        if (map.get(restaurantId) != null) {
            return map.get(restaurantId).poll();
        }
        return null;
    }

    public static void addElement(long restaurantId, String element) {
        if (map.get(restaurantId) != null) {
            map.put(restaurantId, new LinkedList<>());
        }
        map.get(restaurantId).add(element);
    }
}
